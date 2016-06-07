package com.jaskirat.event.tracker.com.jaskirat.event.preference;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.jaskirat.event.tracker.PathNavigationActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by jass on 4/10/2016.
 */
public class RouteParsingTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

    ProgressDialog progressDialog;
    PathNavigationActivity mcontext;
    StringBuilder s;

    public String getJsonData(String urlpath) {
        s = new StringBuilder();
        try {
            URL url = new URL(urlpath);
            URLConnection urlConnection = url.openConnection();
            InputStream inputStream = urlConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String data = bufferedReader.readLine();
            while (data != null) {
                s.append(data);
                data = bufferedReader.readLine();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return s.toString();
    }

    public RouteParsingTask(PathNavigationActivity mcontext) {
        this.mcontext = mcontext;
        progressDialog = new ProgressDialog(mcontext);
        progressDialog.setTitle("Searching...");
        progressDialog.setMessage("Location");
        progressDialog.setCancelable(false);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog.show();

    }

    private List<LatLng> decodePoly(String encoded) {

        List<LatLng> poly = new ArrayList<LatLng>();
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;

        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            LatLng p = new LatLng((((double) lat / 1E5)),
                    (((double) lng / 1E5)));
            poly.add(p);
        }

        return poly;
    }

    @Override
    protected List<List<HashMap<String, String>>> doInBackground(String... params) {
        HashMap<String, String> map;

        List<HashMap<String, String>> path;

        List<List<HashMap<String, String>>> maproutes = new ArrayList<List<HashMap<String, String>>>();


        try {
            JSONObject jsonObject = new JSONObject(getJsonData(params[0]));
            JSONArray routes = jsonObject.getJSONArray("routes");
            for (int i = 0; i < routes.length(); i++) {
                JSONObject obj = routes.getJSONObject(i);
                path = new ArrayList<>();

                JSONArray legs = obj.getJSONArray("legs");

                for (int j = 0; j < legs.length(); j++) {
                    JSONObject object = legs.getJSONObject(j);
                    JSONArray steps = object.getJSONArray("steps");

                    for (int p = 0; p < steps.length(); p++) {
                        JSONObject plo = steps.getJSONObject(p);

                        JSONObject polyline = plo.getJSONObject("polyline");
                        String points = polyline.getString("points");
                        List<LatLng> latLng = decodePoly(points);

                        for (int k = 0; k < latLng.size(); k++) {
                            map = new HashMap<>();

                            map.put("lat", Double.toString(((LatLng) latLng.get(k)).latitude));
                            map.put("lng", Double.toString(((LatLng) latLng.get(k)).longitude));
                            path.add(map);
                        }
                    }
                    maproutes.add(path);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return maproutes;
    }

    LatLng origin;
    LatLng destination;

    @Override
    protected void onPostExecute(List<List<HashMap<String, String>>> s) {
        super.onPostExecute(s);
        progressDialog.dismiss();
        ArrayList<LatLng> points = null;
        PolylineOptions lineOptions = null;
        MarkerOptions markerOptions = new MarkerOptions();

        // Traversing through all the routes

        if (s.size() > 0) {
            for (int i = 0; i < s.size(); i++) {
                points = new ArrayList<LatLng>();
                lineOptions = new PolylineOptions();

                // Fetching i-th route
                List<HashMap<String, String>> path = s.get(i);

                // Fetching all the points in i-th route
                for (int j = 0; j < path.size(); j++) {
                    HashMap<String, String> point = path.get(j);
                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    if (j == 0) {
                        origin = new LatLng(lat, lng);
                    } else if (j == path.size() - 1) {
                        destination = new LatLng(lat, lng);
                    }
                    LatLng position = new LatLng(lat, lng);

                    points.add(position);
                }

                // Adding all the points in the route to LineOptions
                lineOptions.addAll(points);
                lineOptions.width(10);
                lineOptions.color(Color.RED);
            }

            // Drawing polyline in the Google Map for the i-th rout

            mcontext.map.clear();
            try {
                mcontext.map.addMarker(new MarkerOptions().position(origin).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)).title("Origin"));
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                mcontext.map.addMarker(new MarkerOptions().position(destination).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE)).title("Destination"));
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                mcontext.map.addPolyline(lineOptions);
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                mcontext.map.animateCamera(CameraUpdateFactory.newLatLngZoom(destination, 12));
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(mcontext, "No location found", Toast.LENGTH_SHORT).show();
        }
    }


}

