package com.jaskirat.event.weather;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.NetworkImageView;
import com.jaskirat.event.location.AppUtility;
import com.jaskirat.event.tracker.MyApplication;
import com.jaskirat.event.tracker.R;

public class MainActivity extends Activity implements OnClickListener {

    //public String Current_URL = "http://api.openweathermap.org/data/2.5/weather?q=";
    public String Current_URL = "http://api.openweathermap.org/data/2.5/forecast/city?q=";
    public String API_KEY = "&APPID=ebb01a2c380a45c925c8787289bf8edf";

    Button btn, forecast_btn;
    TextView temp_tv, min_max_tv, desc_tv, place_tv;
    EditText et;
    NetworkImageView iv;

    ProgressDialog PD;

    String name, id, country, description, icon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_weather_forcast);

        btn = (Button) findViewById(R.id.button1);
        forecast_btn = (Button) findViewById(R.id.forecast);

        forecast_btn.setOnClickListener(this);

        et = (EditText) findViewById(R.id.editText1);

        place_tv = (TextView) findViewById(R.id.place);
        temp_tv = (TextView) findViewById(R.id.temp);
        min_max_tv = (TextView) findViewById(R.id.min_max);
        desc_tv = (TextView) findViewById(R.id.desc);

        iv = (NetworkImageView) findViewById(R.id.icon);

        btn.setOnClickListener(this);

    }

    public void makejsonreq(String full_url) {

        PD.show();

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Method.GET,
                full_url, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {

                try {
                    JSONObject city = response.getJSONObject("city");

                    id = city.getString("id");
                    name = city.getString("name");
                    country = city.getString("country");

                    JSONArray list = response.getJSONArray("list");

                    int arraylen = list.length();

                    for (int i = 0; i < arraylen; i++) {

                        JSONObject object = list.getJSONObject(i);

                        JSONObject main = object.getJSONObject("main");
                        int temp, temp_max, temp_min;
                        temp = (int) (main.getDouble("temp") - 273.15);
                        temp_max = (int) (main.getDouble("temp_max") - 273.15);
                        temp_min = (int) (main.getDouble("temp_min") - 273.15);

                        Log.e("Temp", temp + " " + temp_max + "  " + temp_min);


                        JSONArray weather = object
                                .getJSONArray("weather");

                        JSONObject jo = weather.getJSONObject(0);
                        description = jo.getString("description");
                        icon = jo.getString("icon");


                        String icon_url = "http://openweathermap.org/img/w/"
                                + icon + ".png";

                        ImageLoader imageLoader = MyApplication
                                .getInstance().getImageLoader();

                        place_tv.setText(name + "," + country);

                        temp_tv.setText(temp + "\u2103");
                        min_max_tv.setText(temp_min + "\u2103 /" + temp_max
                                + "\u2103");
                        desc_tv.setText(description);

                        iv.setImageUrl(icon_url, imageLoader);

                        forecast_btn.setVisibility(View.VISIBLE);

                    }
                    // icon url
                } catch (JSONException e) {
                    e.printStackTrace();
                    PD.dismiss();
                }
                PD.dismiss();
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                PD.dismiss();
            }
        });

        MyApplication.getInstance().addToReqQueue(jsonObjReq, "jreq");

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.button1:
                if (AppUtility.isNetworkavailable(MainActivity.this)) {
                    PD = new ProgressDialog(MainActivity.this);
                    PD.setMessage("Loading.....");
                    PD.setCancelable(false);
                    String city = et.getText().toString();
                    String full_url = Current_URL + city + API_KEY;
                    Log.e("TAG", full_url);
                    makejsonreq(full_url);
                } else {
                    AppUtility.showAlert(MainActivity.this);
                }

                break;

            case R.id.forecast:
                if (AppUtility.isNetworkavailable(MainActivity.this)) {
                    Intent forecast_intent = new Intent(getApplicationContext(),
                            Forecast.class);
                    forecast_intent.putExtra("id", id);
                    forecast_intent.putExtra("name", name);
                    startActivity(forecast_intent);
                } else {
                    AppUtility.showAlert(MainActivity.this);
                }

                break;
        }

    }

}
