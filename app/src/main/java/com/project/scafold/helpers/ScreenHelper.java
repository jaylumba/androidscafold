package com.project.scafold.helpers;

import android.app.Activity;
import android.util.DisplayMetrics;

/**
 * Created by jayan on 10/1/2016.
 */

public class ScreenHelper {

    private static final String TAG = ScreenHelper.class.getSimpleName();

    public static int getScreenWidth(Activity activity){
        DisplayMetrics displaymetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        return  displaymetrics.widthPixels;
    }

    public static int getScreenHeight(Activity activity){
        DisplayMetrics displaymetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        return  displaymetrics.heightPixels;
    }

    public static int getOrientation(Activity activity){
        return activity.getResources().getConfiguration().orientation;
    }
}
