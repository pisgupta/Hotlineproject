package com.jaskirat.event.tracker;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;

import com.jaskirat.event.com.jaskirat.event.adapter.TimeZoneEditDialogListAdapter;
import com.jaskirat.event.com.jaskirat.event.adapter.WorldClockException;
import com.jaskirat.event.com.jaskirat.event.adapter.WorldClockTimeZone;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

/**
 * Created by gupta on 6/4/2016.
 */
public class WorldTimeZoneActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getName();
    private static final int DIALOG_TIMEZONE_LIST = 0;

    private WorldClockTimeZone selectedTimeZone = null;

    TimePicker mTimePicker;
    TextView txttimeview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_world_time);
        mTimePicker = (TimePicker) findViewById(R.id.myworldtime);
        txttimeview = (TextView) findViewById(R.id.timedate);
        findViewById(R.id.button_timezone_edit_list).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(DIALOG_TIMEZONE_LIST);
            }
        });
    }

    @Override
    protected Dialog onCreateDialog(int dialogId) {
        AlertDialog dialog;
        switch (dialogId) {
            case DIALOG_TIMEZONE_LIST:
                LayoutInflater li = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View dialogView = li.inflate(R.layout.timezone_edit_dialog_list, null);

                //setup list with timezone and enable filtering
                ListView dialogList = (ListView) dialogView.findViewById(R.id.dialog_list_view);
                final ArrayAdapter<WorldClockTimeZone> adapter = new TimeZoneEditDialogListAdapter(this, CountryTimeZone.getSupportedTimezones());
                dialogList.setAdapter(adapter);
                dialogList.setTextFilterEnabled(true);
                dialogList.setFastScrollEnabled(true);
                dialogList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        ListView listView = (ListView) parent;
                        TimeZoneEditDialogListAdapter adapter = (TimeZoneEditDialogListAdapter) listView.getAdapter();
                        WorldClockTimeZone selectedItem = adapter.getItem(position);
                        dialogItemSelected(selectedItem);
                        dismissDialog(DIALOG_TIMEZONE_LIST);
                    }
                });

                //Search box that is hooked up to the list
                EditText filterText = (EditText) dialogView.findViewById(R.id.dialog_filter_text);
                filterText.addTextChangedListener(new TextWatcher() {
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        //do nothing
                    }

                    public void beforeTextChanged(CharSequence s, int start, int count,
                                                  int after) {
                        //do nothing

                    }

                    public void afterTextChanged(Editable s) {
                        //update adapter data -see Filter implementation
                        adapter.getFilter().filter(s);
                        adapter.notifyDataSetChanged();
                    }
                });

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setView(dialogView);
                dialog = builder.create();

                break;
            default:
                throw new WorldClockException("Unknown dialog -should never happen");
        }
        return dialog;
    }

    private void dialogItemSelected(WorldClockTimeZone selectedItem) {
        //update display to selected value
        String defaultDisplay = selectedItem.getDefaultDisplay();
        String _id = selectedItem.getId();
        String time = new WorldClockTimeZone(TimeZone.getTimeZone(_id), defaultDisplay).toString();
        TimeZone zone = TimeZone.getTimeZone(_id);
        Calendar calTZ = new GregorianCalendar(zone);
        calTZ.setTimeInMillis(new Date().getTime());
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, calTZ.get(Calendar.YEAR));
        cal.set(Calendar.MONTH, calTZ.get(Calendar.MONTH));
        cal.set(Calendar.DAY_OF_MONTH, calTZ.get(Calendar.DAY_OF_MONTH));
        cal.set(Calendar.HOUR_OF_DAY, calTZ.get(Calendar.HOUR_OF_DAY));
        cal.set(Calendar.MINUTE, calTZ.get(Calendar.MINUTE));
        cal.set(Calendar.SECOND, calTZ.get(Calendar.SECOND));
        cal.set(Calendar.MILLISECOND, calTZ.get(Calendar.MILLISECOND));

        Log.e("time", cal.getTime().toString() + " ");
        Date date = cal.getTime();
        int hh = date.getHours();
        int mm = date.getMinutes();
        int yy = date.getYear();
        int MM = date.getMonth();
        int day = date.getDay();
        zone.getDisplayName();

        mTimePicker.setCurrentHour(hh);
        mTimePicker.setCurrentMinute(mm);
        txttimeview.setText(cal.getTime() + "");

        Log.e("time", yy + "-" + MM + "-" + day + ",  " + hh + ":" + mm);
        Log.e("time", zone.getDisplayName());


    }
}
