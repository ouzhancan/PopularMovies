package com.udacity.popularmovies.utilities;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.udacity.popularmovies.data.MovieDbContract;
import com.udacity.popularmovies.data.MovieDbHelper;
import com.udacity.popularmovies.model.Movie;

public class DbUtil {

    public static final int NUMBER_OF_RESULT_PER_PAGE = 20;

    MovieDbHelper dbHelper;

    public DbUtil(MovieDbHelper movieDbHelper) {
        this.dbHelper = movieDbHelper;
    }

    public Cursor getAllFavorites(){
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        return db.query(
                MovieDbContract.FavoriteEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                MovieDbContract.FavoriteEntry._ID
        );
    }

    public String getMovieById(String movieId){

        String returnId="";

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(MovieDbContract.FavoriteEntry.TABLE_NAME,
                null,
                MovieDbContract.FavoriteEntry.COLUMN_MOVIE_ID+ "=?",
                new String[]{movieId},
                null,
                null,
                null);

        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            returnId = cursor.getString(cursor.getColumnIndex(MovieDbContract.FavoriteEntry.COLUMN_MOVIE_ID));
            cursor.close();
        }

        return returnId;
    }

    public boolean addRemoveFavorite(Movie vMovie,String vGenre) {


        String controlMovieId = getMovieById(vMovie.getId());

        boolean result;

        // if the movie already exists in db, remove otherwise add
        if(controlMovieId!=null && controlMovieId!="" ){
            result = removeFavorite(new Long(vMovie.getId()));
        }else{
            result = addFavorite(vMovie,vGenre);
        }

        return result;
    }

    public boolean addFavorite(Movie addedMovie,String addedGenres) {


        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(MovieDbContract.FavoriteEntry.COLUMN_MOVIE_ID, addedMovie.getId());
        values.put(MovieDbContract.FavoriteEntry.COLUMN_MOVIE_TITLE,addedMovie.getTitle());
        values.put(MovieDbContract.FavoriteEntry.COLUMN_MOVIE_GENRES,addedGenres);
        values.put(MovieDbContract.FavoriteEntry.COLUMN_MOVIE_TAGLINE,addedMovie.getTagline());
        values.put(MovieDbContract.FavoriteEntry.COLUMN_MOVIE_VOTE,addedMovie.getVote_average().toString());
        values.put(MovieDbContract.FavoriteEntry.COLUMN_MOVIE_RELEASE_DATE,addedMovie.getRelease_date());
        values.put(MovieDbContract.FavoriteEntry.COLUMN_MOVIE_HOMEPAGE,addedMovie.getHomepage());
        values.put(MovieDbContract.FavoriteEntry.COLUMN_MOVIE_IMDB,addedMovie.getImdb_id());
        values.put(MovieDbContract.FavoriteEntry.COLUMN_MOVIE_OVERVIEW,addedMovie.getOverview());
        values.put(MovieDbContract.FavoriteEntry.COLUMN_POSTER_PATH,addedMovie.getPoster_path());
        values.put(MovieDbContract.FavoriteEntry.COLUMN_BACKDROP_PATH,addedMovie.getBackdrop_path());

        long id = db.insert(MovieDbContract.FavoriteEntry.TABLE_NAME, null, values);

        db.close();

        return id > 0;

    }

    public boolean removeFavorite(long movieId){

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        int conc = db.delete(MovieDbContract.FavoriteEntry.TABLE_NAME, MovieDbContract.FavoriteEntry.COLUMN_MOVIE_ID+" = "+movieId,null);
        db.close();

        return  conc > 0;
    }


}
