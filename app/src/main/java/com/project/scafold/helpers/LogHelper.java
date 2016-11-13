package com.project.scafold.helpers;

import android.util.Log;

import com.project.scafold.BuildConfig;


/**
 * Created by jayan on 8/27/2016.
 */
public class LogHelper {
    public static void log(final String key, final String message) {
        if (BuildConfig.DEBUG) {
            Log.d(key,message);
        }
    }
}
