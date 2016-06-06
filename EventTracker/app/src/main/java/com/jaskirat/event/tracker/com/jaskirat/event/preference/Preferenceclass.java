package com.jaskirat.event.tracker.com.jaskirat.event.preference;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by gupta on 5/28/2016.
 */
public class Preferenceclass {
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    public Preferenceclass(Context mContext) {
        preferences = mContext.getSharedPreferences("number", Context.MODE_APPEND);
    }


    public String getAmbulane() {
        return preferences.getString("ambulance", "");
    }

    public void setAmbulane(String ambulane) {
        editor = preferences.edit();
        editor.putString("ambulance", ambulane);
        editor.commit();

    }

    public String getFire() {
        return preferences.getString("fire", "");
    }

    public void setFire(String fire) {
        editor = preferences.edit();
        editor.putString("fire", fire);
        editor.commit();

    }

    public String getPolice() {
        return preferences.getString("police", "");
    }

    public void setPolice(String police) {
        editor = preferences.edit();
        editor.putString("police", police);
        editor.commit();
    }

    public boolean getFlag() {
        return preferences.getBoolean("flag", false);
    }

    public void setFlag(boolean flag) {
        editor = preferences.edit();
        editor.putBoolean("flag", flag);
        editor.commit();
    }

    public String getEmail() {
        return preferences.getString("email", "");
    }

    public void setEmail(String email) {
        editor = preferences.edit();
        editor.putString("email", email);
        editor.commit();
    }
}
