package com.udacity.popularmovies.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.udacity.popularmovies.data.MovieDbContract.FavoriteEntry;

public class MovieDbHelper extends SQLiteOpenHelper {

    // The database name
    private static final String DATABASE_NAME = "movie.db";

    // If you change the database schema, you must increment the database version
    private static final int DATABASE_VERSION = 2;

    // Constructor
    public MovieDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create a table to hold waitlist data
        final String SQL_CREATE_FAVORITE_TABLE = "CREATE TABLE " + FavoriteEntry.TABLE_NAME + " (" +
                FavoriteEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                FavoriteEntry.COLUMN_MOVIE_ID + " TEXT NOT NULL, " +
                FavoriteEntry.COLUMN_MOVIE_TITLE + " TEXT NOT NULL, " +
                FavoriteEntry.COLUMN_MOVIE_GENRES + " TEXT , " +
                FavoriteEntry.COLUMN_MOVIE_TAGLINE + " TEXT , " +
                FavoriteEntry.COLUMN_MOVIE_VOTE + " TEXT , " +
                FavoriteEntry.COLUMN_MOVIE_RELEASE_DATE + " TEXT , " +
                FavoriteEntry.COLUMN_MOVIE_HOMEPAGE + " TEXT , " +
                FavoriteEntry.COLUMN_MOVIE_IMDB + " TEXT , " +
                FavoriteEntry.COLUMN_MOVIE_OVERVIEW + " TEXT , " +
                FavoriteEntry.COLUMN_POSTER_PATH + " TEXT , " +
                FavoriteEntry.COLUMN_BACKDROP_PATH + " TEXT " +
                "); ";

        db.execSQL(SQL_CREATE_FAVORITE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + FavoriteEntry.TABLE_NAME);
        onCreate(db);
    }
}
