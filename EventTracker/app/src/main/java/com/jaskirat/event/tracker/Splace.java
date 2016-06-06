package com.jaskirat.event.tracker;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.jaskirat.event.tracker.com.jaskirat.event.preference.Preferenceclass;

public class Splace extends AppCompatActivity {
    Preferenceclass preferenceclass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splace);
        preferenceclass = new Preferenceclass(this);

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);

                    if (!preferenceclass.getFlag()) {
                        Intent it = new Intent(Splace.this, AddNumberActivity.class);
                        startActivity(it);
                    }else {
                        Intent it = new Intent(Splace.this, MainActivity.class);
                        startActivity(it);
                    }

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }

    @Override
    protected void onStop() {
        super.onStop();
        Splace.this.finish();
    }
}
