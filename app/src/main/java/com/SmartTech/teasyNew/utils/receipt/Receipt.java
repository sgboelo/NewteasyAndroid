package com.SmartTech.teasyNew.utils.receipt;

import android.content.Context;
import android.graphics.Bitmap;

import zj.com.customize.sdk.Other;

public abstract class Receipt {

    protected static byte[] INIT = {0x1B, 0x40};
    protected static byte[] NEW_LINE = {0x0A};
    protected static byte[] ALIGN_LEFT = {0x1B, 0x61, 0x00};
    protected static byte[] ALIGN_CENTER = {0x1B, 0x61, 0x01};
    protected static byte[] ALIGN_RIGHT = {0x1B, 0x61, 0x02};
    protected static byte[] EMPHASIZE_ON = {0x1B, 0x45, 0x01};
    protected static byte[] EMPHASIZE_OFF = {0x1B, 0x45, 0x00};
    protected static byte[] FONT_12X24 = {0x1B, 0x4D, 0x00};
    protected static byte[] FONT_9X17 = {0x1B, 0x4D, 0x01};
    protected static byte[] FONT_SCALE_X1 = {0x1D, 0x21, 0x00};
    protected static byte[] FONT_SCALE_X2 = {0x1D, 0x21, 0x11};
    protected static byte[] CHAR_SPACING_0 = {0x1B, 0x20, 0x00};
    protected static byte[] CHAR_SPACING_1 = {0x1B, 0x20, 0x01};

    protected Context context;

    public void setContext(Context context) {
        this.context = context;
    }

    protected static byte[] leftRightAlign(String left, String right, char space) {
        int lineWidth = 384;
        int maxCharsPerLine = lineWidth / 12;    //assuming font 0x00 (12x24)

        StringBuilder sb = new StringBuilder(left);
        for(int i = 0; i < 4; ++i) {
            sb.append(space);
        }

        StringBuilder rightPartBuffer = new StringBuilder();
        StringBuilder nextPart = new StringBuilder();
        for(int i = 0; i < right.length(); ++i) {
            char c = right.charAt(i);
            if(isDivider(c) || i == right.length() - 1) {
                if(sb.length() + nextPart.length() + rightPartBuffer.length() + 1 < maxCharsPerLine) {
                    rightPartBuffer.append(nextPart).append(c);
                    nextPart = new StringBuilder();
                }
                else {
                    String currentLineRightPart = rightPartBuffer.toString().trim();
                    for(int j = sb.length(); j < (maxCharsPerLine - currentLineRightPart.length()); ++j) {
                        sb.append(space);
                    }

                    sb.append(currentLineRightPart);
                    sb.append("\n");
                    sb.append(new String(leftRightAlign("", right.substring(rightPartBuffer.length()), ' ')));
                    return sb.toString().getBytes();
                }
            }
            else {
                nextPart.append(c);
            }
        }

        for(int i = sb.length(); i < (maxCharsPerLine - right.length()); ++i) {
            sb.append(space);
        }

        sb.append(right);
        return sb.toString().getBytes();
    }

    protected byte[] printLogo(Bitmap mBitmap, int nWidth) {
        int nMode = 0;
        int width = ((nWidth + 7) / 8) * 8;
        int height = mBitmap.getHeight() * width / mBitmap.getWidth();
        height = ((height + 7) / 8) * 8;

        Bitmap rszBitmap = mBitmap;
        if (mBitmap.getWidth() != width){
            rszBitmap = Other.resizeImage(mBitmap, width, height);
        }

        Bitmap grayBitmap = Other.toGrayscale(rszBitmap);
        Bitmap alignedBitmap = bmpAlignCenter(grayBitmap);

        byte[] dithered = Other.thresholdToBWPic(alignedBitmap);

        rszBitmap.recycle();
        grayBitmap.recycle();

        return Other.eachLinePixToCmd(dithered, alignedBitmap.getWidth(), nMode);
    }

    protected Bitmap bmpAlignCenter(Bitmap bitmap) {
        int canvasWidth = 384;
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();

        int[] pixels = new int[w * h];

        bitmap.getPixels(pixels, 0, w, 0, 0, w, h);

        int[] destPixels = new int[canvasWidth * h];
        int offset = (canvasWidth - w) / 2;
        int nextPixIndex = 0;
        for(int i = 0; i < h; ++i) {
            for(int j = 0; j < offset; ++j) {
                destPixels[nextPixIndex++] = -1;
            }

            for(int j = 0; j < w; ++j) {
                destPixels[nextPixIndex++] = pixels[i * w + j];
            }
        }

        Bitmap result = Bitmap.createBitmap(canvasWidth - offset, h, Bitmap.Config.ARGB_8888);
        result.setPixels(destPixels, 0, result.getWidth(), 0, 0, result.getWidth(), result.getHeight());

        return result;
    }

    private static boolean isDivider(char c) {
        switch (c) {
            case ' ':
            case '/':
                return true;

            default:
                return false;
        }
    }

    public abstract byte[] generateReceipt();

}
