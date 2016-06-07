package com.jaskirat.event.tracker;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.media.Image;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.jaskirat.event.location.AppUtility;
import com.jaskirat.event.location.LocationResult;
import com.jaskirat.event.location.MyLocation;
import com.jaskirat.event.tracker.com.jaskirat.event.preference.GetImageFile;
import com.jaskirat.event.tracker.com.jaskirat.event.preference.Preferenceclass;
import com.jaskirat.event.tracker.com.jaskirat.event.preference.SharedData;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ImageCapturSend extends AppCompatActivity implements LocationResult {
    ImageView imageView, btnretake, btnsend;

    String currentLocation;
    Preferenceclass preferenceclass;
    String filename;
    MyLocation location;

    @Override
    public void gotLocation(Location location) {
        new MyLoactionTask().execute(location);
    }

    class MyLoactionTask extends AsyncTask<Location, String, String> {
        @Override
        protected String doInBackground(Location... params) {
            Geocoder geocoder = new Geocoder(ImageCapturSend.this, Locale.getDefault());
            String result = null;
            try {
                List<Address> addressList = geocoder.getFromLocation(
                        params[0].getLatitude(), params[0].getLongitude(), 1);
                if (addressList != null && addressList.size() > 0) {
                    Address address = addressList.get(0);
                    StringBuilder sb = new StringBuilder();
                    for (int i = 0; i < address.getMaxAddressLineIndex(); i++) {
                        sb.append(address.getAddressLine(i)).append("\n");
                    }
                    sb.append(address.getLocality()).append("\n");
                    //sb.append(address.getPostalCode()).append("\n");
                    sb.append(address.getCountryName());
                    result = sb.toString();
                }
            } catch (IOException e) {
                Log.e("TAG", "Unable connect to Geocoder", e);
            }
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            currentLocation = s;

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_captur_save);
        preferenceclass = new Preferenceclass(this);
        location = new MyLocation(this);
        location.getLocation(this, this);

        filename = getIntent().getExtras().getString("fileName");

        Log.e("File Name", filename + "");

        imageView = (ImageView) findViewById(R.id.clicked_image);
        btnretake = (ImageView) findViewById(R.id.btn_retake);
        btnsend = (ImageView) findViewById(R.id.btn_send);

        byte[] data = GetImageFile.getImageFromSDCard(filename);

        imageView.setImageBitmap(GetImageFile.getBitmapFromBytes(data));

        btnretake.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ImageCapturSend.this, ImageCapture.class));
            }
        });

        btnsend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    if (AppUtility.isNetworkavailable(ImageCapturSend.this)) {
                        final Intent emailIntent = new Intent(
                                android.content.Intent.ACTION_SEND_MULTIPLE);

                        emailIntent.setType("plain/text");

                        emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL,
                                new String[]{"yournews@nzherald.co.nz"});

                        emailIntent.putExtra(
                                android.content.Intent.EXTRA_SUBJECT,
                                "Emergencie : ");

                        emailIntent
                                .putExtra(android.content.Intent.EXTRA_TEXT, emailBody());

                        emailIntent.putExtra(Intent.EXTRA_STREAM, getUriListForImages1());

                        startActivity(Intent.createChooser(emailIntent, "Send mail..."));
                    } else {
                        AppUtility.showAlert(ImageCapturSend.this);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }


    private ArrayList<Uri> getUriListForImages1() throws Exception {
        ArrayList<Uri> myList = new ArrayList<Uri>();
        String imageDirectoryPath = Environment.getExternalStorageDirectory()
                .getAbsolutePath() + "/Camerafolder/";


        try {
            ContentValues values = new ContentValues(7);
            values.put(MediaStore.Images.Media.TITLE, filename);
            values.put(MediaStore.Images.Media.DISPLAY_NAME, filename);
            values.put(MediaStore.Images.Media.DATE_TAKEN, new Date().getTime());
            values.put(MediaStore.Images.Media.MIME_TYPE, "image/png");
            values.put(MediaStore.Images.ImageColumns.BUCKET_ID,
                    imageDirectoryPath.hashCode());
            values.put(MediaStore.Images.ImageColumns.BUCKET_DISPLAY_NAME,
                    filename);
            filename = filename + ".png";
            Log.e("Image", imageDirectoryPath);
            Log.e("Image", filename);
            values.put("_data", imageDirectoryPath + filename);
            ContentResolver contentResolver = getApplicationContext()
                    .getContentResolver();
            Uri uri = contentResolver.insert(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            myList.add(uri);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return myList;
    }

    private String emailBody() {

        StringBuilder sb = new StringBuilder();


        sb.append("Date of emergence : ");
        sb.append(getTodayDate());
        sb.append("\n\n image : ");
        sb.append(filename);
        sb.append("\n\nUser Location : ");
        sb.append(currentLocation);
        return sb.toString();
    }

    private SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

    private String getTodayDate() {
        Date date = Calendar.getInstance().getTime();
        return format.format(date);
    }
}
