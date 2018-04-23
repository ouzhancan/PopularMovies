package com.udacity.popularmovies.data;

import android.net.Uri;
import android.provider.BaseColumns;

public class MovieDbContract implements BaseColumns {


    public static final String CONTENT_AUTHORITY = "com.udacity.popularmovies";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH = "moviedb";

    public static final class FavoriteEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH).build();


        public static final String TABLE_NAME = "favorite";

        public static final String COLUMN_MOVIE_ID = "movieId";
        public static final String COLUMN_MOVIE_TITLE = "movieTitle";
        public static final String COLUMN_MOVIE_GENRES = "movieGenres";
        public static final String COLUMN_MOVIE_TAGLINE = "movieTagline";
        public static final String COLUMN_MOVIE_VOTE = "movieVote";
        public static final String COLUMN_MOVIE_RELEASE_DATE = "movieReleaseDate";
        public static final String COLUMN_MOVIE_HOMEPAGE = "movieHomepage";
        public static final String COLUMN_MOVIE_IMDB = "movieImdb";
        public static final String COLUMN_MOVIE_OVERVIEW = "movieOverview";
        public static final String COLUMN_POSTER_PATH = "posterPath";
        public static final String COLUMN_BACKDROP_PATH = "backdropPath";


    }

}
