package com.jaskirat.event.tracker;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by gupta on 5/28/2016.
 */
public class MyDialog extends AlertDialog {
    DialogClick click;
    interface  DialogClick{
        public void onDialogButtonClick(View view,View child);
    }
    protected MyDialog(final Context context, String title, String msg, final View view, final DialogClick click) {
        super(context);
        this.click = click;
        TextView mstitle = (TextView) view.findViewById(R.id.mytitle);
        TextView msmessage = (TextView) view.findViewById(R.id.mymessage);

        ImageView imageCall = (ImageView) view.findViewById(R.id.btncall);
        ImageView imageMessage = (ImageView) view.findViewById(R.id.btnmessage);
        ImageView imageCancel = (ImageView) view.findViewById(R.id.btncancel);

        mstitle.setText(title);
        msmessage.setText(msg);
        this.setCancelable(false);
        this.setView(view);
        imageCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                click.onDialogButtonClick(v,view);

            }
        });
        imageMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                click.onDialogButtonClick(v,view);
            }
        });
        imageCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                click.onDialogButtonClick(v,view);
            }
        });


//
//        this.setButton(DialogInterface.BUTTON_POSITIVE, "Message", new OnClickListener() {
//            @Overridex
//            public void onClick(DialogInterface dialog, int which) {
//
//            }
//        });
//        this.setButton(DialogInterface.BUTTON_NEGATIVE, "Call", new OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//
//            }
//        });
//
//        this.setButton(DialogInterface.BUTTON_NEUTRAL, "Cancel", new OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                MyDialog.this.dismiss();
//            }
//        });

    }
}
