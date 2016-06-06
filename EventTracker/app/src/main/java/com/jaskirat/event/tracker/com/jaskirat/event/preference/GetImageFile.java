package com.jaskirat.event.tracker.com.jaskirat.event.preference;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Environment;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by gupta on 5/29/2016.
 */
public class GetImageFile {

    public static byte[] getImageFromSDCard(String imageName) {
        try {
            File myFile = new File(Environment.getExternalStorageDirectory()
                    + "/Camerafolder/" + imageName + ".png");

            InputStream is = new FileInputStream(myFile);
            long length = myFile.length();

            if (length > Integer.MAX_VALUE) {
                // File is too large
            }

            byte[] bytes = new byte[(int) length];

            int offset = 0;
            int numRead = 0;
            while (offset < bytes.length
                    && (numRead = is.read(bytes, offset, bytes.length - offset)) >= 0) {

                offset += numRead;
            }

            if (offset < bytes.length) {
                throw new IOException("Could not completely read file "
                        + myFile.getName());
            }
            is.close();
            return bytes;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Bitmap getBitmapFromBytes(byte[] data) {
        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inJustDecodeBounds = true;

        Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length, o);

        int scale = 1;
        while (o.outWidth / scale / 2 >= SharedData.getInstance()
                .getScreenWidth()
                && o.outHeight / scale / 2 >= SharedData.getInstance()
                .getScreenHeight())
            scale *= 2;

        // Decode with inSampleSize
        BitmapFactory.Options o2 = new BitmapFactory.Options();
        o2.inSampleSize = scale;
        bitmap = BitmapFactory.decodeByteArray(data, 0, data.length, o2);

        return rotateBitmap(bitmap, "180");
    }

    private static Bitmap rotateBitmap(Bitmap bitmap, String degrees) {
        Matrix mat = new Matrix();
        mat.postRotate(Float.parseFloat(degrees));
        Bitmap rotatedBitmap = Bitmap.createBitmap(bitmap, 0, 0,
                bitmap.getWidth(), bitmap.getHeight(), mat, true);
        return rotatedBitmap;
    }

}
