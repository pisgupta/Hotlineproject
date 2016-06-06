package com.jaskirat.event.tracker;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.jaskirat.event.media.FileWriteListener;
import com.jaskirat.event.media.FileWriteThread;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ImageCapture extends AppCompatActivity implements
        SurfaceHolder.Callback, Camera.PictureCallback, View.OnClickListener, FileWriteListener {

    Camera camera;
    SurfaceView surfaceView;
    SurfaceHolder surfaceHolder;
    boolean previewing = false;
    LayoutInflater controlInflater = null;
    RelativeLayout parentLayout;
    ProgressDialog progressDialog;
    ImageView imageCapture;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        // setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        imageCapture = (ImageView) findViewById(R.id.btncapture);
        getWindow().setFormat(PixelFormat.UNKNOWN);
        surfaceView = (SurfaceView) findViewById(R.id.camerapreview);
        surfaceHolder = surfaceView.getHolder();
        surfaceHolder.addCallback(this);
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

        imageCapture.setOnClickListener(this);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height) {
        if (previewing) {
            camera.stopPreview();
            previewing = false;
        }

        if (camera != null) {
            try {
                camera.setPreviewDisplay(surfaceHolder);
                camera.startPreview();
                previewing = true;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        try {
            camera = Camera.open();
            camera.setDisplayOrientation(90);
        } catch (Exception e) {
            Log.e("TAG", "surfaceCreated:");

        }


    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        camera.stopPreview();
        camera.release();
        camera = null;
        previewing = false;
    }

    @Override
    public void onClick(View v) {

        try {
            Log.e("Picture Taken", "Picture Taken");
            camera.takePicture(null, null, this);
            Log.e("Picture Taken", "Picture Taken");
        } catch (Exception e) {

            Log.e("Picture Taken", e.toString() + "");
        }

    }

    @Override
    public void onPictureTaken(byte[] data, Camera camera) {
        Log.e("onPictureTaken", "onPictureTaken");
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        fileName = "IMG_" + timeStamp;

        FileWriteThread fileWriteThread = new FileWriteThread(fileName, data, this);
        fileWriteThread.start();
    }
    String fileName;

    /**
     * This method is used to check that which Camera is to be used. (Front or
     * rear)
     *
     * @return the camera in use
     */

    private Camera openFrontFacingCameraGingerbread() {
        int cameraCount = 0;
        Camera cam = null;
        Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
        cameraCount = Camera.getNumberOfCameras();

        Log.e("No of cameras ", "-------------- " + cameraCount);

        for (int camIdx = 0; camIdx < cameraCount; camIdx++) {
            Camera.getCameraInfo(camIdx, cameraInfo);
            if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                try {
                    cam = Camera.open(camIdx);
                } catch (RuntimeException e) {
                    Log.e("CAMERA",
                            "Camera failed to open: " + e.getLocalizedMessage());
                }
            }
        }
        return cam;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void fileWritten() {
        Intent intent = new Intent(this, ImageCapturSend.class);
        intent.putExtra("fileName",fileName);
        startActivity(intent);
        finish();
        progressDialog.dismiss();
    }

    @Override
    public void onBackPressed() {
        //   Utilities.getAlertDialog(this, this);
    }
}
