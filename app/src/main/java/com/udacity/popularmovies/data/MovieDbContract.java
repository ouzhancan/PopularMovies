package com.udacity.popularmovies.data;

import android.provider.BaseColumns;

public class MovieDbContract {

    public static final class FavoriteEntry implements BaseColumns {

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
