package com.udacity.popularmovies.retrofit;

import com.udacity.popularmovies.utilities.NetworkUtil;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by ouz on 24/02/18.
 * source : https://www.journaldev.com/13639/retrofit-android-example-tutorial
 */

public class APIClient {

    private static Retrofit retrofit = null;
    private static OkHttpClient.Builder client = null;

    public static Retrofit getClient() {

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        client = new OkHttpClient.Builder();
        client.addInterceptor(interceptor);

        if (retrofit != null) {
            retrofit = null;
        }

        if(client!=null){
            retrofit = new Retrofit.Builder()
                    .baseUrl(NetworkUtil.BASE_MOVIE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client.build())
                    .build();
        }

        return retrofit;
    }



}
