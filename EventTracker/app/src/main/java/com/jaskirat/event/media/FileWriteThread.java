package com.jaskirat.event.media;

/**
 * This class runs in the background and writes the image
 * captured to the SD card.
 */

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Environment;
import android.util.Log;

import com.jaskirat.event.tracker.com.jaskirat.event.preference.SharedData;

public class FileWriteThread extends Thread {

    String imageName;
    byte[] data;
    FileWriteListener fileWriteListener;

    public FileWriteThread(String imageName, byte[] data,
                           FileWriteListener fileWriteListener) {
        this.imageName = imageName;
        this.data = data;
        this.fileWriteListener = fileWriteListener;
    }

    @Override
    public void run() {

        File file1 = new File(Environment.getExternalStorageDirectory()
                + "/Camerafolder/");
        if (!file1.exists()) {
            boolean isCreated = file1.mkdirs();
            Log.e("Directory Created", "Directory Created " + isCreated);
        }

        File file = new File(Environment.getExternalStorageDirectory()
                + "/Camerafolder/" + imageName + ".png");

        byte[] bytesAfterAlter = bitmapOperations(data);

        data = null;

        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(file);
            fileOutputStream.write(bytesAfterAlter);
            bytesAfterAlter = null;
            Log.e("FILE Written", "FILE Written");
        } catch (Exception e) {
        } finally {
            try {
                fileOutputStream.close();
            } catch (Exception e2) {
            }
        }

        data = null;

        Log.e("FILE READ", "FILE READ");

        if (fileWriteListener != null) {
            fileWriteListener.fileWritten();
        }
    }

    private static Bitmap getBitmapFromBytes(byte[] data) {
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

        return bitmap;
    }

    private static Bitmap rotateBitmap(Bitmap bitmap, String degrees) {
        Matrix mat = new Matrix();
        mat.postRotate(Float.parseFloat(degrees));
        Bitmap rotatedBitmap = Bitmap.createBitmap(bitmap, 0, 0,
                bitmap.getWidth(), bitmap.getHeight(), mat, true);
        return rotatedBitmap;
    }

    private static Bitmap flipBitmap(Bitmap bitmap) {
        float[] mirrorY = {-1, 0, 0, 0, 1, 0, 0, 0, 1};

        Matrix matrixMirrorY = new Matrix();
        matrixMirrorY.setValues(mirrorY);

        Matrix matrix = new Matrix();
        matrix.postConcat(matrixMirrorY);

        Bitmap mirrorBitmap = Bitmap.createBitmap(bitmap, 0, 0,
                bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        return mirrorBitmap;
    }

    private static byte[] getBytesFromBitmap(Bitmap bitmap) {
        ByteArrayOutputStream stream = null;
        byte[] byteArray = null;

        try {
            stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byteArray = stream.toByteArray();
            bitmap.recycle();
            stream.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                stream.close();
                stream = null;
            } catch (Exception e2) {
            }
        }
        return byteArray;
    }

    private static byte[] bitmapOperations(byte[] data) {

        Bitmap bitmap = getBitmapFromBytes(data);
        String degrees;
        degrees = "270";
        bitmap = rotateBitmap(bitmap, degrees);
        byte[] returnBytes;
        bitmap = flipBitmap(bitmap);
        returnBytes = getBytesFromBitmap(bitmap);
        bitmap.recycle();
        return returnBytes;
    }
}
