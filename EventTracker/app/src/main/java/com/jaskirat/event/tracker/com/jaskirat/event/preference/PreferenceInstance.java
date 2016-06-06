package com.jaskirat.event.tracker.com.jaskirat.event.preference;

import android.content.Context;

/**
 * Created by gupta on 5/31/2016.
 */
public class PreferenceInstance {
    static Preferenceclass instance;

    public static Preferenceclass getPreferenceInstance(Context mContext) {
        if (instance == null) {
            instance = new Preferenceclass(mContext);
            return instance;
        } else {
            return instance;
        }
    }
}
