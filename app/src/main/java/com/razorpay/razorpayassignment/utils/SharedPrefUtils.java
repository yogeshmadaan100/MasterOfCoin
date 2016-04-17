package com.razorpay.razorpayassignment.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.razorpay.razorpayassignment.R;
import com.razorpay.razorpayassignment.models.SortType;

/**
 * Created by yogeshmadaan on 16/04/16.
 */
public class SharedPrefUtils {
    private static final int MODE = Context.MODE_PRIVATE;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    public static final String KEY_LAST_TIMESTAMP ="timestamp";
    public static final String KEY_SERVER_STATUS ="serverStatus";
    public static final String KEY_SORT_CRITERIA ="sortCriteria";
    public SharedPrefUtils(Context context) {
        sharedPreferences = getPreferences(context);
        editor = getEditor(context);
    }


    public SharedPreferences getPreferences(Context context) {
        String appName = context.getResources().getString(R.string.app_name);
        return context.getSharedPreferences(appName, MODE);
    }

    public void clearSharedPreferenceFile(Context context) {
        getEditor(context).clear().apply();
    }

    public SharedPreferences.Editor getEditor(Context context) {
        return getPreferences(context).edit();
    }

    public long getLastTimestamp()
    {
        return sharedPreferences.getLong(KEY_LAST_TIMESTAMP,0);
    }

    public void setLastTimestamp(Long timestamp)
    {
        editor.putLong(KEY_LAST_TIMESTAMP,timestamp);
        editor.commit();
    }

    public String getServerStatus()
    {
        return sharedPreferences.getString(KEY_SERVER_STATUS,"OFFLINE");
    }

    public void setServerStatus(String status)
    {
        editor.putString(KEY_SERVER_STATUS,status);
        editor.commit();
    }
    public String getSortCriteria()
    {
        return sharedPreferences.getString(KEY_SORT_CRITERIA, SortType.NONE.toString());
    }

    public void setSortCriteria(String status)
    {
        Log.e("setting sort criteria ",""+status);
        editor.putString(KEY_SORT_CRITERIA,status);
        editor.commit();
    }



}
