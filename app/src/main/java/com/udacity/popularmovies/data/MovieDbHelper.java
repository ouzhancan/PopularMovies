package com.udacity.popularmovies.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MovieDbHelper extends SQLiteOpenHelper {

    // The database name
    private static final String DATABASE_NAME = "movie.db";

    // If you change the database schema, you must increment the database version
    private static final int DATABASE_VERSION = 3;

    // Constructor
    public MovieDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create a table to hold waitlist data
        final String SQL_CREATE_FAVORITE_TABLE = "CREATE TABLE " + MovieDbContract.FavoriteEntry.TABLE_NAME + " (" +
                MovieDbContract.FavoriteEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                MovieDbContract.FavoriteEntry.COLUMN_MOVIE_ID + " TEXT NOT NULL, " +
                MovieDbContract.FavoriteEntry.COLUMN_MOVIE_TITLE + " TEXT NOT NULL, " +
                MovieDbContract.FavoriteEntry.COLUMN_MOVIE_GENRES + " TEXT , " +
                MovieDbContract.FavoriteEntry.COLUMN_MOVIE_TAGLINE + " TEXT , " +
                MovieDbContract.FavoriteEntry.COLUMN_MOVIE_VOTE + " TEXT , " +
                MovieDbContract.FavoriteEntry.COLUMN_MOVIE_RELEASE_DATE + " TEXT , " +
                MovieDbContract.FavoriteEntry.COLUMN_MOVIE_HOMEPAGE + " TEXT , " +
                MovieDbContract.FavoriteEntry.COLUMN_MOVIE_IMDB + " TEXT , " +
                MovieDbContract.FavoriteEntry.COLUMN_MOVIE_OVERVIEW + " TEXT , " +
                MovieDbContract.FavoriteEntry.COLUMN_POSTER_PATH + " TEXT , " +
                MovieDbContract.FavoriteEntry.COLUMN_BACKDROP_PATH + " TEXT " +
                "); ";

        db.execSQL(SQL_CREATE_FAVORITE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + MovieDbContract.FavoriteEntry.TABLE_NAME);
        onCreate(db);
    }
}
