package com.example.flytta.flyttatestapp.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;

/**
 * Created by WIN 1O on 18-03-2018.
 */

public class NetworkUtility {

    public static boolean isNetworkAvailable(Context context){
        boolean isNetworlAvailable= false;

        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if(networkInfo!=null)
        {
            isNetworlAvailable = networkInfo.getState() == NetworkInfo.State.CONNECTED;
        }
        return isNetworlAvailable;
    }
}
