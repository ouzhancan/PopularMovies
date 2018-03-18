package com.udacity.popularmovies.retrofit;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.udacity.popularmovies.utilities.NetworkUtil;

import java.text.DateFormat;
import java.util.Date;

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

            // Create a first instance a Gson
            Gson gson = new GsonBuilder()
                    .setDateFormat("yyyy-MM-dd")
                    .create();

            // Get the date adapter
            TypeAdapter<Date> dateTypeAdapter = gson.getAdapter(Date.class);

            // Ensure the DateTypeAdapter is null safe
            TypeAdapter<Date> safeDateTypeAdapter = dateTypeAdapter.nullSafe();


            gson = new GsonBuilder()
                    .setDateFormat("yyyy-MM-dd")
                    .registerTypeAdapter(Date.class, safeDateTypeAdapter)
                    .create();

            GsonConverterFactory factory = GsonConverterFactory.create(gson);

            retrofit = new Retrofit.Builder()
                    .baseUrl(NetworkUtil.BASE_MOVIE_URL)
                    .addConverterFactory(factory)
                    .client(client.build())
                    .build();
        }

        return retrofit;
    }



}
