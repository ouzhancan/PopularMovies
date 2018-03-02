package com.udacity.popularmovies.utilities;

import android.net.Uri;
import android.util.Log;

import com.udacity.popularmovies.BuildConfig;
import com.udacity.popularmovies.model.MovieContainer;
import com.udacity.popularmovies.retrofit.APIClient;
import com.udacity.popularmovies.retrofit.APIInterface;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NetworkUtil {

    private static final String TAG = NetworkUtil.class.getSimpleName();

    public static final String BASE_MOVIE_URL = "https://api.themoviedb.org/3/movie/";
    public static final String BASE_IMAGE_URL = "http://image.tmdb.org/t/p/";

    public static final String POPULAR_MOVIE_PATH = "popular";
    public static final String TOP_RATED_MOVIE_PATH = "top_rated";
    public static final String MOVIE_IMAGE_PATH = "images";
    public static final String IMAGE_SIZE = "w185";

    // retrofit api service variable
    public static APIInterface apiService;

    // movie container //
    private static MovieContainer movieContainer = null;


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

    public static MovieContainer getMoviesWithAPI(String option){

        // Retrofit service //
        apiService = APIClient.getClient().create(APIInterface.class);

        Call<MovieContainer> movieContainerCall;

        if(option.equalsIgnoreCase(TOP_RATED_MOVIE_PATH)){
            movieContainerCall = apiService.getTopRatedMovies(BuildConfig.MOVIE_DB_API_KEY);
        }else{
            movieContainerCall = apiService.getPopularMovies(BuildConfig.MOVIE_DB_API_KEY);
        }

        if(movieContainerCall.isExecuted() && movieContainerCall != null){

            movieContainerCall.enqueue(new Callback<MovieContainer>() {

                @Override
                public void onResponse(Call<MovieContainer> call, Response<MovieContainer> response) {

                    if (response.isSuccessful()) {
                         movieContainer = response.body();
                    }
                }

                @Override
                public void onFailure(Call<MovieContainer> call, Throwable t) {
                    Log.d("DEMO_LOG", t.getLocalizedMessage());
                }
            });
        }
        // *** *** Retrofit Service *** *** //

        return movieContainer;
    }

}
