package com.jaskirat.event.com.jaskirat.event.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.jaskirat.event.location.AppUtility;
import com.jaskirat.event.tracker.EventModel;
import com.jaskirat.event.tracker.R;

import java.util.ArrayList;

/**
 * Created by Pankaj on 5/31/2016.
 */
public class EventDisplayAdapter extends BaseAdapter {
    Context mContext;
    LayoutInflater inflater;
    ArrayList<EventModel> eventList;

    public EventDisplayAdapter(Context mContext, ArrayList<EventModel> eventList) {
        this.mContext = mContext;
        this.eventList = eventList;
        inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public int getCount() {
        return eventList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            mViewHolder = new ViewHolder();
            convertView = inflater.inflate(R.layout.event_row_layout, parent, false);
            mViewHolder.txttitle = (TextView) convertView.findViewById(R.id.txttitle);
            mViewHolder.txtpubdate = (TextView) convertView.findViewById(R.id.txtpubdate);
            mViewHolder.txtlink = (TextView) convertView.findViewById(R.id.txtlink);
            convertView.setTag(mViewHolder);

        } else {
            mViewHolder = (ViewHolder) convertView.getTag();
        }
        mViewHolder.txttitle.setText(eventList.get(position).getTitle());
        mViewHolder.txtpubdate.setText(eventList.get(position).getPubdate());
        mViewHolder.txtlink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (AppUtility.isNetworkavailable(mContext)) {
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(eventList.get(position).getLink()));
                    mContext.startActivity(i);
                } else {
                    AppUtility.showAlert(mContext);
                }
            }
        });
        return convertView;
    }

    ViewHolder mViewHolder;

    class ViewHolder {
        TextView txttitle;
        TextView txtpubdate;
        TextView txtlink;
    }
}
