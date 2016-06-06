package com.jaskirat.event.tracker;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.jaskirat.event.com.jaskirat.event.adapter.EventDisplayAdapter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class EventTrakerActivity extends AppCompatActivity {
    ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_traker);
        mListView = (ListView) findViewById(R.id.display_event);
        MyEventTask task = new MyEventTask(this);
        task.execute();
    }

    ArrayList<EventModel> models;

    class MyEventTask extends AsyncTask {
        ProgressDialog mProgressDialog;

        public MyEventTask(Context mContext) {
            mProgressDialog = new ProgressDialog(EventTrakerActivity.this);
            mProgressDialog.setTitle("Loading...");
            mProgressDialog.setCancelable(false);
            mProgressDialog.setMessage("Downloading Data");
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog.show();
        }

        @Override
        protected Object doInBackground(Object[] params) {
            InputStream stream = GetRSSEventData.getEventUrl("http://www.eventfinda.co.nz/feed/events/auckland/whatson/upcoming.rss");
            models = new XmlParsing().parseData(stream);


            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            EventDisplayAdapter adapter = new EventDisplayAdapter(EventTrakerActivity.this, models);
            mListView.setAdapter(adapter);
            mProgressDialog.dismiss();
        }
    }
}