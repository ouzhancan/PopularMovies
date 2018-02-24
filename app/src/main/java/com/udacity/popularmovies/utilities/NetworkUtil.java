package com.udacity.popularmovies.utilities;

import android.net.Uri;
import android.util.Log;

import com.udacity.popularmovies.BuildConfig;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by ouz on 24/02/18.
 */

public class NetworkUtil {

    private static final String TAG = NetworkUtil.class.getSimpleName();

    private static final String BASE_MOVIE_URL = "http://api.themoviedb.org/3";
    private static final String BASE_IMAGE_URL = " http://image.tmdb.org/t/p";

    public static final String POPULAR_MOVIE_PATH = "/movie/popular";
    public static final String TOP_RATED_MOVIE_PATH = "/movie/top_rated";
    private static final String IMAGE_SIZE = "/w185";

    private static final String QUERY_API_KEY = "apiKey";

    /**
     * built movie query uri in order to selection
     * @param selectionPath : /movie/popular or /movie/top_rated
     * @return
     */
    public static URL buildMovieUrl(String selectionPath) {

        Uri builtUri = Uri.parse(BASE_MOVIE_URL+selectionPath).buildUpon()
                .appendQueryParameter(QUERY_API_KEY, BuildConfig.MOVIE_DB_API_KEY)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    /**
     * built the request link of image for picasso
     * @param posterPath : the poster image path of movie
     * @return
     */
    public static URL buildImageUrl(String posterPath) {

        Uri builtUri = Uri.parse(BASE_IMAGE_URL+IMAGE_SIZE+posterPath).buildUpon()
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

}
