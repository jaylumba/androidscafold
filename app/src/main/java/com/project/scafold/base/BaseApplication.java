package com.project.scafold.base;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jakewharton.picasso.OkHttp3Downloader;
import com.project.scafold.BuildConfig;
import com.project.scafold.R;
import com.project.scafold.helpers.AppConstants;
import com.project.scafold.helpers.DaoHelper;
import com.project.scafold.helpers.LogHelper;
import com.project.scafold.interfaces.ApiInterface;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.concurrent.Executors;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * Created by jayan on 8/27/2016.
 */
public class BaseApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/Roboto-Regular.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );


        DaoHelper.initialize(this);

        if (AppConstants.PICASSO == null) {

            File cacheDir = getExternalCacheDir();
            if (cacheDir == null) {
                cacheDir = getCacheDir();
            }
            final Cache cache = new Cache(cacheDir, 10 * 1024 * 1024);

            final OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .addInterceptor(
                            chain -> {
                                Request request = chain.request();
                                LogHelper.log("api", "performing url --> " + request.url());
                                return chain.proceed(request);
                            })
                    .cache(cache)
                    .build();

            /** initialize picasso */
            AppConstants.PICASSO = new Picasso.Builder(this)
                    .executor(Executors.newSingleThreadExecutor())
                    .downloader(new OkHttp3Downloader(okHttpClient)).build();

        }

        if (AppConstants.RETROFIT == null && AppConstants.API_INTERFACE == null) {

            File cacheDir = getExternalCacheDir();
            if (cacheDir == null) {
                cacheDir = getCacheDir();
            }
            final Cache cache = new Cache(cacheDir, 10 * 1024 * 1024);

            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            // set your desired log level
            if (BuildConfig.DEBUG)
                logging.setLevel(HttpLoggingInterceptor.Level.BODY);
            else
                logging.setLevel(HttpLoggingInterceptor.Level.NONE);

            final OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .addInterceptor(logging)
                    .cache(cache)
                    .build();

            /** initialize picasso */
            AppConstants.PICASSO = new Picasso.Builder(this)
                    .executor(Executors.newSingleThreadExecutor())
                    .downloader(new OkHttp3Downloader(okHttpClient)).build();

            Gson GSON = new GsonBuilder()
                    .serializeNulls()
                    .create();

            /** initialize retrofit */
            AppConstants.RETROFIT = new Retrofit.Builder()
                    .baseUrl(BuildConfig.BASE_URL)
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create(GSON))
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .build();

            /** initialize retrofit interface */
            AppConstants.API_INTERFACE = AppConstants.RETROFIT.create(ApiInterface.class);
        }
    }

    /**
     * check network connection availability
     */
    public boolean isNetworkAvailable() {
        boolean isConnected = false;
        ConnectivityManager connManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (mWifi.isConnected()) {
            isConnected = true;
        } else {
            NetworkInfo mData = connManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            if (mData == null) {
                isConnected = false;
            } else {
                boolean isDataEnabled = mData.isConnected();
                isConnected = isDataEnabled ? true : false;
            }
        }
        return isConnected;
    }
}
