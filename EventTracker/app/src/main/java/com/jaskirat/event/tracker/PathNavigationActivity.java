package com.jaskirat.event.tracker;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.jaskirat.event.location.AppUtility;
import com.jaskirat.event.location.LocationResult;
import com.jaskirat.event.location.MyLocation;
import com.jaskirat.event.tracker.com.jaskirat.event.preference.MyConstantAPI;
import com.jaskirat.event.tracker.com.jaskirat.event.preference.RouteParsingTask;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class PathNavigationActivity extends AppCompatActivity implements OnMapReadyCallback, LocationResult {


    EditText edtorigin, edtdestination;
    ImageView btngetpath;
    MyLocation location;
    SupportMapFragment supportMapFragment;
    public GoogleMap map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_path_navigation);

        location = new MyLocation(PathNavigationActivity.this);
        location.getLocation(PathNavigationActivity.this, this);

        edtorigin = (EditText) findViewById(R.id.edtorigin);
        edtdestination = (EditText) findViewById(R.id.edtdestination);
        btngetpath = (ImageView) findViewById(R.id.btngetpath);
        supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mymap);
        supportMapFragment.getMapAsync(this);


        btngetpath.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (TextUtils.isEmpty(edtorigin.getText().toString().trim())) {
                    edtorigin.setError("Please enter origin");
                    edtorigin.requestFocus();
                    return;
                }

                if (TextUtils.isEmpty(edtdestination.getText().toString().trim())) {
                    edtdestination.setError("Please enter destination");
                    edtdestination.requestFocus();
                    return;
                }


                if (AppUtility.isNetworkavailable(PathNavigationActivity.this)) {
                    String origin = edtorigin.getText().toString().trim();
                    String destination = edtdestination.getText().toString().trim();
                    String urlpath = MyConstantAPI.BASE_URL + MyConstantAPI.ORIGIN + origin + MyConstantAPI.DESTINATION + destination + MyConstantAPI.VOID + MyConstantAPI.MODE + MyConstantAPI.API_KEY;
                    RouteParsingTask task = new RouteParsingTask(PathNavigationActivity.this);
                    task.execute(urlpath);
                    Log.e("Url Path = ", urlpath);
                } else {
                    AppUtility.showAlert(PathNavigationActivity.this);
                }

            }
        });

        edtdestination.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                edtdestination.setError(null);
            }
        });


        edtorigin.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                edtorigin.setError(null);
            }
        });


    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.map = googleMap;
        this.map.setMyLocationEnabled(true);
        this.map.getUiSettings().setZoomControlsEnabled(true);

    }

    @Override
    public void gotLocation(Location location) {
        new MyTest().execute(location);
    }


    class MyTest extends AsyncTask<Location, Integer, Location> {
        @Override
        protected Location doInBackground(Location... params) {

            return params[0];
        }

        @Override
        protected void onPostExecute(Location location) {
            super.onPostExecute(location);
            map.addMarker(new MarkerOptions().position(new LatLng(location.getLatitude(), location.getLongitude())));
            map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 10));


        }
    }

}
