package com.SmartTech.teasyNew.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import com.google.common.io.Files;
import org.apache.commons.lang3.RandomStringUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

/**
 * Created by muddvayne on 15/03/2017.
 */

public class ImageUtils {

    private static final int PHOTO_MAX_DIMENSION_SIZE = 600;

    private static final int PHOTO_QUALITY = 75;

    private static Logger logger = Logger.getLogger(ImageUtils.class.getCanonicalName());

    /**
     * Creates empty file with random name
     * */
    public static File createImageFile(File storageDir) throws IOException {
        File image;
        do {
            image = new File(storageDir, RandomStringUtils.randomNumeric(10) + ".jpg");
        } while (image.exists());

        image.createNewFile();

        return image;
    }

    /**
     * Creates compressed image from the source image file.
     * Note that it doesn't delete or replace source file
     *
     * @param sourceImageFile Source file of the image which should be compressed
     *
     * @param destFile File to write compressed image
     *
     * @param maxSize Max size of images's width/height. The greater dimension will be resized to this value;
     * the smaller will be resized proportionally. For instance, if this param equals to 800 and source image
     * has size 1600x1200, it will be resized to 800x600
     *
     * @param quality Quality of the photo. Used for compression. Can take value from 0 to 100
     *
     * @return Compressed image's File object, or null if exception was thrown
     * */
    public static File compressImage(File sourceImageFile, File destFile, int maxSize, int quality) {
        Bitmap bitmap = scaleImage(sourceImageFile, maxSize);

        //fix photo orientation
        int rotation = getExifRotation(sourceImageFile.getAbsolutePath());
        if(rotation > 0) {
            Matrix matrix = new Matrix();
            matrix.postRotate(rotation);
            bitmap = Bitmap.createBitmap(bitmap , 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        }

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, quality, stream);
        byte[] byteArray = stream.toByteArray();

        if(destFile.exists()) {
            destFile.delete();
        }
        try {
            Files.write(byteArray, destFile);
            return destFile;
        }
        catch (IOException e) {
            logger.warning("Image has not been compressed. " + e.toString());
            e.printStackTrace();

            if(destFile.exists()) {
                destFile.delete();
            }

            return null;
        }
    }

    public static Bitmap scaleImage(File sourceImageFile, int maxSize) {
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(sourceImageFile.getAbsolutePath(), bmOptions);

        int currentH = bmOptions.outHeight;
        int currentW = bmOptions.outWidth;

        if(currentH < maxSize && currentW < maxSize) {
            maxSize = Math.max(currentH, currentW);
        }

        float scaleFactor = Math.max((float)currentW/(float)maxSize,
                (float)currentH/(float)maxSize);

        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = (int)scaleFactor;
        bmOptions.inPurgeable = true;

        Bitmap bitmap = BitmapFactory.decodeFile(sourceImageFile.getAbsolutePath(), bmOptions);

        /*
         *   BitmapFactory.Options.inSampleSize takes integer value, so that
         *   we have to scale image manually :(
         * */

        if(scaleFactor > 1) {
            int targetH, targetW;
            if(currentH > currentW) {
                targetH = Math.min(maxSize, currentH);
                targetW = (int)(currentW / scaleFactor);
            }
            else {
                targetW = Math.min(maxSize, currentW);
                targetH = (int)(currentH / scaleFactor);
            }

            bitmap = Bitmap.createScaledBitmap(bitmap, targetW, targetH, true);
        }

        return bitmap;
    }

    public static File compressImage(File sourceFile) {
        try {
            File destFile = createImageFile(sourceFile.getParentFile());
            return compressImage(sourceFile, destFile, PHOTO_MAX_DIMENSION_SIZE, PHOTO_QUALITY);
        }
        catch (IOException e) {
            logger.warning("Could not create file");
            e.printStackTrace();

            return null;
        }
    }

    public static int getExifRotation(String imgPath)
    {
        try
        {
            ExifInterface exif = new ExifInterface(imgPath);
            String rotationAmount = exif.getAttribute(ExifInterface.TAG_ORIENTATION);
            if (rotationAmount != null && rotationAmount.length() > 0)
            {
                int rotationParam = Integer.parseInt(rotationAmount);
                switch (rotationParam)
                {
                    case ExifInterface.ORIENTATION_NORMAL:
                        return 0;
                    case ExifInterface.ORIENTATION_ROTATE_90:
                        return 90;
                    case ExifInterface.ORIENTATION_ROTATE_180:
                        return 180;
                    case ExifInterface.ORIENTATION_ROTATE_270:
                        return 270;
                    default:
                        return 0;
                }
            }
            else
            {
                return 0;
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            return 0;
        }
    }

}
