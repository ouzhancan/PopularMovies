package com.udacity.popularmovies.retrofit;


import com.udacity.popularmovies.model.Movie;
import com.udacity.popularmovies.model.MovieContainer;
import com.udacity.popularmovies.model.ReviewContainer;
import com.udacity.popularmovies.model.VideoContainer;
import com.udacity.popularmovies.utilities.NetworkUtil;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface APIInterface {

    /* API GET MOVIES request path variations */
    @GET(NetworkUtil.POPULAR_MOVIE_PATH)
    Call<MovieContainer> getPopularMovies(@Query("api_key")String api_key,@Query("page")String page);

    @GET(NetworkUtil.TOP_RATED_MOVIE_PATH)
    Call<MovieContainer> getTopRatedMovies(@Query("api_key")String api_key,@Query("page")String page);

    @GET(NetworkUtil.UPCOMING_MOVIE_PATH)
    Call<MovieContainer> getUpcomingMovies(@Query("api_key")String api_key,@Query("page")String page);


    @GET("{movie_id}")
    Call<Movie> getMovieById(@Path("movie_id") String movie_id, @Query("api_key")String api_key);

    @GET("{movie_id}/"+NetworkUtil.MOVIE_REVIEW_PATH)
    Call<ReviewContainer> getReviewsByMovieId(@Path("movie_id") String movie_id, @Query("api_key")String api_key);

    @GET("{movie_id}/"+NetworkUtil.MOVIE_VIDEO_PATH)
    Call<VideoContainer> getVideosByMovieId(@Path("movie_id") String movie_id, @Query("api_key")String api_key);

    /* *** *** *** */

}
