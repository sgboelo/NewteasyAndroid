package com.SmartTech.teasyNew.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import com.SmartTech.teasyNew.FontsOverride;
import com.SmartTech.teasyNew.R;
import com.SmartTech.teasyNew.popups.PopupOperationFailed;
import com.SmartTech.teasyNew.utils.ImageUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ActivityPhoto extends Activity {

    private static final Logger logger = Logger.getLogger(ActivityPhoto.class.getCanonicalName());
    private static final int REQUEST_TAKE_PHOTO = 1;

    public static final String PHOTO_FILE_PATH_NAME = "photo";
    public static final String PREVIEW_NAME = "preview";

    private static final int PREVIEW_SIZE = 128;
    private static final int MY_CAMERA_REQUEST_CODE = 100;

    private ImageView mImageView;
    private String mCurrentPhotoPath;
    private Bitmap preview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FontsOverride.setDefaultFont(this, "SERIF", "fonts/Roboto-Medium.ttf");
        FontsOverride.setDefaultFont(this, "MONOSPACE", "fonts/Roboto-Light.ttf");
        setContentView(R.layout.activity_photo);
    }

    @Override
    protected void onStart() {
        // since android 6 camera permission in manifest is ignored and must be requested dynamically
        super.onStart();
        if ( Build.VERSION.SDK_INT >= 23 &&
                ContextCompat.checkSelfPermission( this, android.Manifest.permission.CAMERA ) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.CAMERA},
                    MY_CAMERA_REQUEST_CODE);
        }
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_CAMERA_REQUEST_CODE) {
            if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                PopupOperationFailed failPopup = new PopupOperationFailed(this);
                failPopup.setText("Camera permission is required to take photos");
                failPopup.setButtonOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ActivityPhoto.this.setResult(111);
                        ActivityPhoto.this.finish();
                    }
                });
                failPopup.show();
            }

        }
    }

    public void onPhotoButtonClicked(View view) {
        dispatchTakePictureIntent();
    }

    public void retakePhoto(View view) {
        dispatchTakePictureIntent();
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            //delete current photo if exists
            if(mCurrentPhotoPath != null) {
                File file = new File(mCurrentPhotoPath);
                if(file.exists()) {
                    file.delete();
                }
            }

            // Create the File where the photo should go
            File photoFile = null;
            try {
                File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
                photoFile = ImageUtils.createImageFile(storageDir);
            } catch (IOException ex) {
                logger.warning("Could not create image file");
                ex.printStackTrace();
            }

            if (photoFile != null) {
                mCurrentPhotoPath = photoFile.getAbsolutePath();

                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.milesbreed.teasy2.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);

                List<ResolveInfo> resInfoList = this.getPackageManager().queryIntentActivities(takePictureIntent, PackageManager.MATCH_DEFAULT_ONLY);
                for (ResolveInfo resolveInfo : resInfoList) {
                    String packageName = resolveInfo.activityInfo.packageName;
                    this.grantUriPermission(packageName, photoURI, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
                }

                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_TAKE_PHOTO) {
                setContentView(R.layout.is_photo_ok);
                mImageView = (ImageView) findViewById(R.id.photoPreview);

                mImageView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        setPic();
                    }
                });
            }
        }
    }

    private void setPic() {
        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        // Get the dimensions of the View
        View imageContainer = findViewById(R.id.photoContainer);
        int containerWidth = imageContainer.getWidth();
        float scaleFactor = ((float)photoW/(float)containerWidth);
        int targetH = Math.min((int)(photoH/scaleFactor), imageContainer.getHeight());
        int targetW = Math.min((int)(photoW/scaleFactor), imageContainer.getWidth());

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = (int)scaleFactor;
        bmOptions.inPurgeable = true;

        Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
        bitmap = Bitmap.createScaledBitmap(bitmap,targetW,targetH, true);

        int orientation = ImageUtils.getExifRotation(mCurrentPhotoPath);
        if(orientation > 0) {
            Matrix matrix = new Matrix();
            matrix.postRotate(orientation);
            bitmap = Bitmap.createBitmap(bitmap , 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        }

        preview = Bitmap.createScaledBitmap(bitmap, PREVIEW_SIZE, (int) (((float)bitmap.getHeight()) / bitmap.getWidth() * PREVIEW_SIZE), true);
        mImageView.setImageBitmap(bitmap);

        try {
            FileOutputStream fos = new FileOutputStream(mCurrentPhotoPath);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 75, fos);
            fos.close();
        } catch (IOException ex) {
            logger.log(Level.SEVERE, "Failed to compress file", ex);
        }
    }

    public void photoOk(View view) {
        File file = new File(mCurrentPhotoPath);
        if(file.exists()) {
            Intent resultIntent = new Intent();
            resultIntent.putExtra(PHOTO_FILE_PATH_NAME, mCurrentPhotoPath);
            resultIntent.putExtra(PREVIEW_NAME, preview);
            setResult(RESULT_OK, resultIntent);
            finish();
        }
        else {
            logger.info("Something went wrong, photo does not exist");
            setResult(10);
            finish();

        }
    }

}
