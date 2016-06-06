package com.jaskirat.event.tracker;

import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by Pankaj on 6/22/2015.
 */
public class XmlParsing {
    private ArrayList<EventModel> eventList;
    private EventModel model;
    private String TAG = "XmlParsing";

    /**
     * @param is
     * @return
     */
    public ArrayList<EventModel> parseData(InputStream is) {
        try {
            XmlPullParserFactory xmlFactoryObject = XmlPullParserFactory.newInstance();
            XmlPullParser myparser = xmlFactoryObject.newPullParser();
            myparser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            myparser.setInput(is, null);
            parseXMLAndStoreIt(myparser);
            is.close();

        } catch (Exception ex) {
            Log.e(TAG, ex.toString());
        }
        return eventList;
    }

    public void parseXMLAndStoreIt(XmlPullParser myParser) {
        int event;
        String text = null;
        eventList = new ArrayList<EventModel>();
        try {
            event = myParser.getEventType();
            while (event != XmlPullParser.END_DOCUMENT) {
                String name = myParser.getName();
                switch (event) {
                    case XmlPullParser.START_TAG:
                        if (name.equals("item")) {
                            model = new EventModel();
                        }
                        break;
                    case XmlPullParser.TEXT:
                        text = myParser.getText();
                        break;
                    case XmlPullParser.END_TAG:
                        if (name.equals("item")) {
                            eventList.add(model);
                        } else if (name.equals("title")) {
                            if (model != null) {
                                model.setTitle(text);
                            }
                        } else if (name.equals("link")) {
                            if (model != null) {
                                model.setLink(text);
                            }
                        }  else if (name.equals("pubDate")) {
                            if (model != null) {
                                model.setPubdate(getTime(text));
                            }
                        }
                        break;
                }
                event = myParser.nextToken();
            }
        } catch (Exception e) {
            Log.e(TAG, e.toString() + "");
        }

    }

    public String getTime(String timezone) {
        StringBuilder sb = new StringBuilder();
        String timezone1[] = timezone.split("T");
        sb.append(formatdate(timezone1[0]));
        sb.append(" ");
        sb.append(formateTime(timezone1[1]));
        return sb.toString();
    }

    public String formateTime(String ti) {
        DateFormat f1 = new SimpleDateFormat("HH:mm:ss"); //HH for hour of the day (0 - 23)
        Date d = null;
        try {
            d = f1.parse(ti);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        DateFormat f2 = new SimpleDateFormat("h:mma");
        String time = f2.format(d).toLowerCase(); //
        return time;
    }

    public String formatdate(String timezone) {
        Calendar c = Calendar.getInstance();
        Date date = c.getTime(); //current date and time in UTC
        //SimpleDateFormat df = new SimpleDateFormat("EEE, MMMMM dd, yyyy K:mm a", Locale.US);
        SimpleDateFormat df = new SimpleDateFormat("EEE, MMMMM dd, yyyy ", Locale.US);
        df.setTimeZone(TimeZone.getTimeZone(timezone)); //format in given timezone
        String strDate = df.format(date);
        return strDate;

    }
}
