package com.jaskirat.event.tracker;

import android.app.ProgressDialog;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.jaskirat.event.location.LocationResult;
import com.jaskirat.event.location.MyLocation;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class CallMessage extends AppCompatActivity implements LocationResult {
    String number;
    EditText edtmsg;
    TextView txtlocation;
    MyLocation location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call_message);
        location = new MyLocation(this);
        location.getLocation(this, this);
        edtmsg = (EditText) findViewById(R.id.edtmessage);
        number = getIntent().getExtras().getString("number");
        txtlocation = (TextView) findViewById(R.id.txtcurrentLocation);

        findViewById(R.id.btnsend).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(edtmsg.getText().toString().trim())) {
                    SmsManager.getDefault().sendTextMessage(number, null, edtmsg.getText().toString().trim() + "" + txtlocation.getText(), null, null);
                } else {
                    Toast.makeText(CallMessage.this, "Please enter message.", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    public void gotLocation(Location location) {

        new MyLoactionTask().execute(location);


    }


    class MyLoactionTask extends AsyncTask<Location, String, String> {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            progressDialog = new ProgressDialog(CallMessage.this);
//            progressDialog.setTitle("Location");
//            progressDialog.setMessage("Location loading");
//            progressDialog.setCancelable(false);
//            progressDialog.show();
        }

        @Override
        protected String doInBackground(Location... params) {
            Geocoder geocoder = new Geocoder(CallMessage.this, Locale.getDefault());
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
            //LocationAddress.getAddressFromLocation(location.getLatitude(), location.getLongitude(), this, new GeocoderHandler());
            txtlocation.append(s);

        }
    }


}
