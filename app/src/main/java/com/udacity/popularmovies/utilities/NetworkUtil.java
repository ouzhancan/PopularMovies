package com.udacity.popularmovies.utilities;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.util.Log;

import com.udacity.popularmovies.BuildConfig;
import com.udacity.popularmovies.model.Movie;
import com.udacity.popularmovies.model.MovieContainer;
import com.udacity.popularmovies.model.Review;
import com.udacity.popularmovies.model.ReviewContainer;
import com.udacity.popularmovies.model.Video;
import com.udacity.popularmovies.model.VideoContainer;
import com.udacity.popularmovies.retrofit.APIClient;
import com.udacity.popularmovies.retrofit.APIInterface;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import retrofit2.Call;
import retrofit2.Response;

public class NetworkUtil {

    private static final String TAG = NetworkUtil.class.getSimpleName();

    public static final String BASE_MOVIE_URL = "https://api.themoviedb.org/3/movie/";
    public static final String BASE_IMAGE_URL = "http://image.tmdb.org/t/p/";
    public static final String BASE_IMDB_MOVIE_URL  = "http://www.imdb.com/title/";
    public static final String BASE_YOUTUBE_URL = "http://www.youtube.com/watch?v=";

    public static final String POPULAR_MOVIE_PATH = "popular";
    public static final String TOP_RATED_MOVIE_PATH = "top_rated";
    public static final String UPCOMING_MOVIE_PATH = "upcoming";
    public static final String FAVORITES_MOVIE_PATH = "favorites";

    public static final String MOVIE_VIDEO_PATH = "videos";
    public static final String MOVIE_REVIEW_PATH = "reviews";
    public static final String MOVIE_IMAGE_PATH = "images";
    public static final String IMAGE_SIZE = "w185";
    public static final String IMAGE_BACKDROP_SIZE = "w500";

    // retrofit api service variable
    public static APIInterface apiService;

    // movie container //
    private static MovieContainer movieContainer = null;


    /**
     * built the request link of image for picasso
     *
     * @param posterPath : the poster image path of movie
     * @return
     */
    public static URL buildImageUrl(String posterPath) {

        Uri builtUri = Uri.parse(BASE_IMAGE_URL + IMAGE_SIZE + posterPath).buildUpon()
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
     * built the request link of youtube video
     * @param videoId : youtube video id
     * @return
     */
    public static URL buildVideoUrl(String videoId) {

        Uri builtUri = Uri.parse(BASE_YOUTUBE_URL +videoId).buildUpon()
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
     * get the movies informations via api. option parameter is selected choice.
     * if user select sort popular movies, this method will call getPopularMovies with retrofit.
     * Or select another choice, the retrofit call is rearranged with this choice.
     * <p>
     * Calling operation is synchronously because I use asyncTaskLoader in MainActivity
     *
     * @param option : the url suffix (popular/  or top_rated/ )
     * @return
     */
    public static MovieContainer getMoviesWithAPI(String option,String page) {

        // *** **** Retrofit service **** *** //
        apiService = APIClient.getClient().create(APIInterface.class);

        Call<MovieContainer> movieContainerCall;

        if (option != null && option.equalsIgnoreCase(TOP_RATED_MOVIE_PATH)) {
            // if the option was selected
            movieContainerCall = apiService.getTopRatedMovies(BuildConfig.MOVIE_DB_API_KEY,page);
        }else if(option != null && option.equalsIgnoreCase(UPCOMING_MOVIE_PATH)) {
            movieContainerCall = apiService.getUpcomingMovies(BuildConfig.MOVIE_DB_API_KEY,page);
        }else {
            // default option
            movieContainerCall = apiService.getPopularMovies(BuildConfig.MOVIE_DB_API_KEY,page);
        }

        if (movieContainerCall != null) {

            /* Synchronous calling */
            try {
                Response<MovieContainer> response = movieContainerCall.execute();

                if (response.isSuccessful()) {
                    movieContainer = response.body();
                }
            } catch (IOException ex) {
                Log.d("RETROFIT_CALL", "retrofit synchronous calling was thrown an error -> " + ex.getLocalizedMessage());
            }

            /* Synchronous calling */


            /* Asynchronous calling */
            /* !!! This method cause a problem that is unloaded data. !!! */
            /*
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

            */
            /* Asynchronous calling  */


        }

        // *** *** Retrofit Service *** *** //

        return movieContainer;
    }

    public static Movie getMoviDetailById(String movieId) {

        Movie movie = null;

        // *** **** Retrofit service **** *** //
        apiService = APIClient.getClient().create(APIInterface.class);

        Call<Movie> movieDetailCall;

        if (movieId != null) {
            movieDetailCall = apiService.getMovieById(movieId, BuildConfig.MOVIE_DB_API_KEY);

            if (movieDetailCall != null) {

            /* Synchronous calling */
                try {
                    Response<Movie> response = movieDetailCall.execute();

                    if (response.isSuccessful()) {
                        movie = response.body();
                    }
                } catch (IOException ex) {
                    Log.d("RETROFIT_CALL", "retrofit synchronous calling was thrown an error -> " + ex.getLocalizedMessage());
                }
            }
            /* Synchronous calling end */
        }

        return movie;
    }


    public static VideoContainer getVideosByMovieId(String movieId) {

        VideoContainer videoContainer = null;

        // *** **** Retrofit service **** *** //
        apiService = APIClient.getClient().create(APIInterface.class);

        Call<VideoContainer> videoContainerCall;

        if (movieId != null) {
            videoContainerCall = apiService.getVideosByMovieId(movieId, BuildConfig.MOVIE_DB_API_KEY);

            if (videoContainerCall != null) {

            /* Synchronous calling */
                try {
                    Response<VideoContainer> response = videoContainerCall.execute();

                    if (response.isSuccessful()) {
                        videoContainer = response.body();
                    }
                } catch (IOException ex) {
                    Log.d("RETROFIT_CALL", "retrofit synchronous calling was thrown an error -> " + ex.getLocalizedMessage());
                }
            }
            /* Synchronous calling end */
        }

        return videoContainer;
    }


    public static ReviewContainer getReviewsByMovieId(String movieId) {

        ReviewContainer reviewContainer = null;

        // *** **** Retrofit service **** *** //
        apiService = APIClient.getClient().create(APIInterface.class);

        Call<ReviewContainer> reviewContainerCall;

        if (movieId != null) {
            reviewContainerCall = apiService.getReviewsByMovieId(movieId, BuildConfig.MOVIE_DB_API_KEY);

            if (reviewContainerCall != null) {

            /* Synchronous calling */
                try {
                    Response<ReviewContainer> response = reviewContainerCall.execute();

                    if (response.isSuccessful()) {
                        reviewContainer = response.body();
                    }
                } catch (IOException ex) {
                    Log.d("RETROFIT_CALL", "retrofit synchronous calling was thrown an error -> " + ex.getLocalizedMessage());
                }
            }
            /* Synchronous calling end */
        }

        return reviewContainer;
    }


    /**
     * Check internet connection
     *
     * @return
     */
    public static boolean isThereAConnection(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }


}
