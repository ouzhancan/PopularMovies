package com.udacity.popularmovies.utilities;

import android.net.Uri;

import java.net.MalformedURLException;
import java.net.URL;

public class NetworkUtil {

    private static final String TAG = NetworkUtil.class.getSimpleName();

    public static final String BASE_MOVIE_URL = "https://api.themoviedb.org/3/movie/";
    public static final String BASE_IMAGE_URL = "http://image.tmdb.org/t/p/";

    public static final String POPULAR_MOVIE_PATH = "popular";
    public static final String TOP_RATED_MOVIE_PATH = "top_rated";
    public static final String MOVIE_IMAGE_PATH = "images";
    public static final String IMAGE_SIZE = "w185";


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
