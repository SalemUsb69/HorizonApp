package com.androguro.usb.horizon.app;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by mohamed on 1/7/18.
 */

public class ValuePairs {


    public static String getUserLAT(Context context) {

        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);

        return   pref.getString("user_LAT", "0");
    }


    public static void setUserLAT(String key, Context context) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor edit = pref.edit();
        edit.putString("user_lat", key);
        edit.apply();
    }

    public static String getUserLNG(Context context) {

        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);

        return  pref.getString("user_lng", null);
    }


    public static void setUserLNG(String key, Context context) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor edit = pref.edit();
        edit.putString("user_lng", key);
        edit.apply();
    }
}
