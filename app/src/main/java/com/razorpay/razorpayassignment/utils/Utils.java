package com.razorpay.razorpayassignment.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by yogeshmadaan on 16/04/16.
 */
public class Utils {
    private final static DateFormat isoDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
    private final static DateFormat standardDateFormat = new SimpleDateFormat("dd MMM yyyy hh:mm:ss a");
    private final static DateFormat simpleDateFormat = new SimpleDateFormat("hh:mm:ss");
    public static boolean isConnectedToInternet(Context context) {
        if (context != null) {
            ConnectivityManager cm = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetworkInfo = cm.getActiveNetworkInfo();
            if (activeNetworkInfo != null && activeNetworkInfo.isConnected()) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public static String formatDate(String date)
    {
        try {
            Date timeStamp = isoDateFormat.parse(date);
            return  standardDateFormat.format(timeStamp);
        } catch (ParseException e) {
            return date;
        }
    }

    public static Long convertDatetoTimestamp(String date)
    {
        try {
            Date timeStamp = isoDateFormat.parse(date);
            return  timeStamp.getTime();
        } catch (ParseException e) {
            return 0L;
        }
    }

    public static String formatLastUpdatedTimestamp(Long timestamp)
    {
        return simpleDateFormat.format(new Date(timestamp));
    }
}
