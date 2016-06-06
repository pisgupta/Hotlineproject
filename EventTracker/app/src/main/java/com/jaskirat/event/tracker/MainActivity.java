package com.jaskirat.event.tracker;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.ListView;

import com.jaskirat.event.com.jaskirat.event.adapter.EventDisplayAdapter;
import com.jaskirat.event.location.AppUtility;
import com.jaskirat.event.tracker.com.jaskirat.event.preference.Preferenceclass;
import com.jaskirat.event.tracker.com.jaskirat.event.preference.SharedData;

import java.io.InputStream;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, MyDialog.DialogClick {
    View promptView;
    LayoutInflater layoutInflater;
    Preferenceclass preferenceclass;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        preferenceclass = new Preferenceclass(this);
        layoutInflater = LayoutInflater.from(this);
        promptView = layoutInflater.inflate(R.layout.dialog_view, null);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        SharedData.getInstance().setScreenWidth(metrics.widthPixels);
        SharedData.getInstance().setScreenHeight(metrics.heightPixels);


        findViewById(R.id.btntime).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, WorldTimeZoneActivity.class));
            }
        });


        findViewById(R.id.btnweather).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (AppUtility.isNetworkavailable(MainActivity.this)) {
                    startActivity(new Intent(MainActivity.this, com.jaskirat.event.weather.MainActivity.class));
                } else {
                    AppUtility.showAlert(MainActivity.this);
                }
            }
        });

        findViewById(R.id.btnevent).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (AppUtility.isNetworkavailable(MainActivity.this)) {
                    startActivity(new Intent(MainActivity.this, EventTrakerActivity.class));
                } else {
                    AppUtility.showAlert(MainActivity.this);
                }
            }
        });

        findViewById(R.id.btnpath).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (AppUtility.isNetworkavailable(MainActivity.this)) {
                    startActivity(new Intent(MainActivity.this, PathNavigationActivity.class));
                } else {
                    AppUtility.showAlert(MainActivity.this);
                }
            }
        });
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    MyDialog dialog;
    String callNumber;

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.police) {
            callNumber = preferenceclass.getPolice();
            dialog = new MyDialog(this, getResources().getString(R.string.police), getResources().getString(R.string.msg), promptView, this);
            dialog.show();
        } else if (id == R.id.fire) {
            callNumber = preferenceclass.getFire();
            dialog = new MyDialog(this, getResources().getString(R.string.fire), getResources().getString(R.string.msg), promptView, this);
            dialog.show();
        } else if (id == R.id.ambulance) {
            callNumber = preferenceclass.getAmbulane();
            dialog = new MyDialog(this, getResources().getString(R.string.ambulance), getResources().getString(R.string.msg), promptView, this);
            dialog.show();
        } else if (id == R.id.takepicture) {
            startActivity(new Intent(MainActivity.this, ImageCapture.class));
        } else if (id == R.id.setting) {
            startActivity(new Intent(MainActivity.this, AddNumberActivity.class));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onDialogButtonClick(View v, View view) {
        switch (v.getId()) {
            case R.id.btncall:

                if (view.getParent() != null)
                    ((ViewGroup) view.getParent()).removeView(view);
                dialog.dismiss();
                Intent it = new Intent(Intent.ACTION_CALL);
                it.setData(Uri.parse("tel:" + callNumber));
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                startActivity(it);
                break;
            case R.id.btnmessage:
                if (AppUtility.isGPSOnOf(MainActivity.this) && AppUtility.isNetworkavailable(MainActivity.this)) {
                    if (view.getParent() != null)
                        ((ViewGroup) view.getParent()).removeView(view);
                    dialog.dismiss();
                    Intent msg = new Intent(MainActivity.this, CallMessage.class);
                    msg.putExtra("number", callNumber);
                    startActivity(msg);
                } else {
                    AppUtility.showAlert(MainActivity.this);
                }
                break;
            case R.id.btncancel:
                if (view.getParent() != null)
                    ((ViewGroup) view.getParent()).removeView(view);
                dialog.dismiss();

                break;

        }
    }
}
