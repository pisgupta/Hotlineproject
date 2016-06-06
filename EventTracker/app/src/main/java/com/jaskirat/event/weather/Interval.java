package com.jaskirat.event.weather;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import com.jaskirat.event.tracker.R;

public class Interval extends Activity {

	TextView tv;
	ListView lv;
	CustomAdapter adapter;
	List<RowItem> rowItems = new ArrayList<RowItem>();

	String place_id, place_name, mDate;

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.interval);

		tv = (TextView) findViewById(R.id.idate_tv);
		lv = (ListView) findViewById(R.id.ilv);

		adapter = new CustomAdapter(getApplicationContext(), rowItems);
		lv.setAdapter(adapter);

		Intent i = getIntent();

		place_id = i.getExtras().getString("placeid");
		place_name = i.getExtras().getString("name");
		mDate = i.getExtras().getString("mDate");
		String jo = i.getExtras().getString("jo");

		//getActionBar().setSubtitle(place_name);
		tv.setText(mDate);
		parseJson(jo, mDate);

	}

	private void parseJson(String jobj, String mDate) {

		try {
			JSONObject jsonobj = new JSONObject(jobj);

			JSONArray jlist = jsonobj.getJSONArray("list");

			// tv.append(ja.length() + "");

			for (int i = 0; i < jlist.length(); i++) {

				String dt_txt = jlist.getJSONObject(i).getString("dt_txt");

				String jDate = dt_txt.substring(0, 10);

				if (mDate.equals(jDate)) {

					JSONObject main = jlist.getJSONObject(i).getJSONObject(
							"main");

					int temp, temp_max, temp_min;

					temp = (int) (main.getDouble("temp") - 273.15);
					temp_max = (int) (main.getDouble("temp_max") - 273.15);
					temp_min = (int) (main.getDouble("temp_min") - 273.15);

					JSONArray weather = jlist.getJSONObject(i).getJSONArray(
							"weather");

					String icon = weather.getJSONObject(0).getString("icon");
					String description = weather.getJSONObject(0).getString(
							"description");

					String mtime = dt_txt.substring(11, 16);

					RowItem rItem = new RowItem(temp, temp_max, temp_min,
							description, icon, mtime);

					rowItems.add(rItem);

				} // if end

			} // for end

			adapter.notifyDataSetChanged();

		} catch (JSONException e) {

			e.printStackTrace();
		}

	}

}
