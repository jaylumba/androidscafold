package com.project.scafold.helpers;

import com.project.scafold.interfaces.ApiInterface;
import com.squareup.picasso.Picasso;

import retrofit2.Retrofit;

/**
 * Created by jayan on 10/8/2016.
 */

public class AppConstants {

    /** static instance of Picasso */
    public static Picasso PICASSO = null;

    /** static instance of Web Service Objects */
    public static Retrofit RETROFIT = null;
    public static ApiInterface API_INTERFACE = null;

    /** api actions */
    public static final String ACTION_LOGIN = "action login";


}
