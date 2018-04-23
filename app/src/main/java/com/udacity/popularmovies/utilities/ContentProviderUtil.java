package com.udacity.popularmovies.utilities;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.udacity.popularmovies.MainActivity;
import com.udacity.popularmovies.data.MovieDbContract;
import com.udacity.popularmovies.data.MovieDbHelper;
import com.udacity.popularmovies.model.Movie;


public class ContentProviderUtil extends ContentProvider {

    MovieDbHelper movieDbHelper;

    protected ContentValues contentValues = new ContentValues();

    public static final int FAVORITES = 100;
    public static final int FAVORITES_WITH_ID = 101;

    private static final UriMatcher sUriMatcher = buildUriMatcher();

    Context context;


    public static UriMatcher buildUriMatcher() {

        // Initialize a UriMatcher with no matches by passing in NO_MATCH to the constructor
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

        uriMatcher.addURI(MovieDbContract.CONTENT_AUTHORITY, MovieDbContract.PATH, FAVORITES);
        uriMatcher.addURI(MovieDbContract.CONTENT_AUTHORITY, MovieDbContract.PATH + "/#",
                FAVORITES_WITH_ID);

        return uriMatcher;
    }

    public ContentProviderUtil() {
        super();
    }

    public ContentProviderUtil(Context mContext) {
        this.context = mContext;
        movieDbHelper = new MovieDbHelper(context);
    }

    @Override
    public boolean onCreate() {
        context = getContext();
        movieDbHelper = new MovieDbHelper(context);
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection,
                        @Nullable String selection, @Nullable String[] selectionArgs,
                        @Nullable String sortOrder) {


        final SQLiteDatabase db = movieDbHelper.getReadableDatabase();
        // Write URI match code and set a variable to return a Cursor
        int match = sUriMatcher.match(uri);
        Cursor retCursor;

        // Query for the tasks directory and write a default case
        switch (match) {
            // Query for the tasks directory
            case FAVORITES:
                retCursor = db.query(MovieDbContract.FavoriteEntry.TABLE_NAME,
                        projection,
                        selection==null?null:selection+" = ?",
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            // Default exception
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        // Set a notification URI on the Cursor and return that Cursor
        retCursor.setNotificationUri(context.getContentResolver(), uri);

        // Return the desired Cursor
        return retCursor;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {

        final SQLiteDatabase db = movieDbHelper.getWritableDatabase();

        int match = sUriMatcher.match(uri);
        Uri returnUri;

        switch (match) {
            case FAVORITES:
                // Insert new values into the database
                // Inserting values into tasks table
                long id = db.insert(MovieDbContract.FavoriteEntry.TABLE_NAME,
                        null, values);
                if (id > 0) {
                    returnUri = ContentUris.withAppendedId(uri, id);
                } else {
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                }
                break;
            // Set the value for the returnedUri and write the default case for unknown URI's
            // Default case throws an UnsupportedOperationException
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        // Notify the resolver if the uri has been changed, and return the newly inserted URI
        context.getContentResolver().notifyChange(uri, null);

        // Return constructed uri (this points to the newly inserted row of data)
        return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {


        final SQLiteDatabase db = movieDbHelper.getWritableDatabase();

        int match = sUriMatcher.match(uri);

        int deleted = 0;

        // Write the code to delete a single row of data
        // [Hint] Use selections to delete an item by its row ID
        switch (match) {
            // Handle the single item case, recognized by the ID included in the URI path
            case FAVORITES_WITH_ID:
                // Use selections/selectionArgs to filter for this ID
                deleted = db.delete(MovieDbContract.FavoriteEntry.TABLE_NAME,
                        selection + " = ?", selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        // Notify the resolver of a change and return the number of items deleted
        if (deleted != 0) {
            // A task was deleted, set notification
            context.getContentResolver().notifyChange(uri, null);
        }

        // Return the number of tasks deleted
        return deleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values,
                      @Nullable String selection,
                      @Nullable String[] selectionArgs) {
        //Keep track of if an update occurs
        int tasksUpdated;

        // match code
        int match = sUriMatcher.match(uri);

        switch (match) {
            case FAVORITES_WITH_ID:
                //update a single task by getting the id
                String id = uri.getPathSegments().get(1);
                //using selections
                tasksUpdated = movieDbHelper.getWritableDatabase().update(
                        MovieDbContract.FavoriteEntry.TABLE_NAME, values,
                        "_id=?", new String[]{id});
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        if (tasksUpdated != 0) {
            //set notifications if a task was updated
            context.getContentResolver().notifyChange(uri, null);
        }

        // return number of tasks updated
        return tasksUpdated;
    }

    public Cursor getAllFavorites(Uri mUri) {

        return query(mUri, null, null, null, null);
    }

    public boolean isFavorite(String movieId) {

        Cursor cursor = query(MovieDbContract.FavoriteEntry.CONTENT_URI, null,
                MovieDbContract.FavoriteEntry.COLUMN_MOVIE_ID,
                new String[]{movieId}, null);

        if (cursor != null && cursor.getCount() > 0) {
            return true;
        } else {
            return false;
        }

    }

    public Movie getMovieById(String id) {

        Cursor cursor = query(MovieDbContract.FavoriteEntry.CONTENT_URI, null,
                MovieDbContract.FavoriteEntry.COLUMN_MOVIE_ID,
                new String[]{id}, null);

        Movie movie;
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();

            movie = new Movie();

            movie.setId(cursor.getString(cursor.getColumnIndex(
                    MovieDbContract.FavoriteEntry.COLUMN_MOVIE_ID)));

            movie.setTitle(cursor.getString(cursor.getColumnIndex(
                    MovieDbContract.FavoriteEntry.COLUMN_MOVIE_TITLE)));

            movie.setGenre_ids(MainActivity.stringToGenres(cursor.getString(cursor.getColumnIndex(
                    MovieDbContract.FavoriteEntry.COLUMN_MOVIE_GENRES))));

            movie.setTagline(cursor.getString(cursor.getColumnIndex(
                    MovieDbContract.FavoriteEntry.COLUMN_MOVIE_TAGLINE)));

            movie.setVote_average(Double.valueOf(cursor.getString(cursor.getColumnIndex(
                    MovieDbContract.FavoriteEntry.COLUMN_MOVIE_VOTE))));

            movie.setRelease_date(cursor.getString(cursor.getColumnIndex(
                    MovieDbContract.FavoriteEntry.COLUMN_MOVIE_RELEASE_DATE)));

            movie.setHomepage(cursor.getString(cursor.getColumnIndex(
                    MovieDbContract.FavoriteEntry.COLUMN_MOVIE_HOMEPAGE)));

            movie.setImdb_id(cursor.getString(cursor.getColumnIndex(
                    MovieDbContract.FavoriteEntry.COLUMN_MOVIE_IMDB)));

            movie.setOverview(cursor.getString(cursor.getColumnIndex(
                    MovieDbContract.FavoriteEntry.COLUMN_MOVIE_OVERVIEW)));

            movie.setPoster_path(cursor.getString(cursor.getColumnIndex(
                    MovieDbContract.FavoriteEntry.COLUMN_POSTER_PATH)));

            movie.setBackdrop_path(cursor.getString(cursor.getColumnIndex(
                    MovieDbContract.FavoriteEntry.COLUMN_BACKDROP_PATH)));

            return movie;
        } else {
            return null;
        }
    }

    public boolean addRemoveFavorite(Uri uri, Movie vMovie, String vGenre) {

        boolean conc;
        if (isFavorite(vMovie.getId())) {
            // if the movie already exists in db, remove otherwise add
            conc = removeFavorite(uri, vMovie.getId());
        } else {
            conc = addFavorite(uri, vMovie, vGenre);
        }

        return conc;
    }

    private boolean addFavorite(Uri mUri, Movie mMovie, String mGenres) {

        Uri returnedUri = null;

        if (mMovie != null && !isFavorite(mMovie.getId())) {
            returnedUri = insert(mUri, getContentValues(mMovie, mGenres));
        }

        return returnedUri == null ? false : true;
    }

    private boolean removeFavorite(Uri mUri, String movieId) {
        int rowNum = 0;
        if (movieId != null && isFavorite(movieId)) {
            rowNum = delete(mUri,
                    MovieDbContract.FavoriteEntry.COLUMN_MOVIE_ID,
                    new String[]{movieId});
        }
        return rowNum > 0;
    }

    private ContentValues getContentValues(Movie mMovie, String mGenres) {

        ContentValues values = new ContentValues();

        values.put(MovieDbContract.FavoriteEntry.COLUMN_MOVIE_ID, mMovie.getId());
        values.put(MovieDbContract.FavoriteEntry.COLUMN_MOVIE_TITLE, mMovie.getTitle());
        values.put(MovieDbContract.FavoriteEntry.COLUMN_MOVIE_GENRES, mGenres);
        values.put(MovieDbContract.FavoriteEntry.COLUMN_MOVIE_TAGLINE, mMovie.getTagline());
        values.put(MovieDbContract.FavoriteEntry.COLUMN_MOVIE_VOTE, mMovie.getVote_average().toString());
        values.put(MovieDbContract.FavoriteEntry.COLUMN_MOVIE_RELEASE_DATE, mMovie.getRelease_date());
        values.put(MovieDbContract.FavoriteEntry.COLUMN_MOVIE_HOMEPAGE, mMovie.getHomepage());
        values.put(MovieDbContract.FavoriteEntry.COLUMN_MOVIE_IMDB, mMovie.getImdb_id());
        values.put(MovieDbContract.FavoriteEntry.COLUMN_MOVIE_OVERVIEW, mMovie.getOverview());
        values.put(MovieDbContract.FavoriteEntry.COLUMN_POSTER_PATH, mMovie.getPoster_path());
        values.put(MovieDbContract.FavoriteEntry.COLUMN_BACKDROP_PATH, mMovie.getBackdrop_path());

        return values;
    }

    @Override
    public String getType(@NonNull Uri uri) {

        int match = sUriMatcher.match(uri);

        switch (match) {
            case FAVORITES:
                // directory
                return "vnd.android.cursor.dir" + "/" + MovieDbContract.CONTENT_AUTHORITY + "/"
                        + MovieDbContract.PATH;
            case FAVORITES_WITH_ID:
                // single item type
                return "vnd.android.cursor.item" + "/" + MovieDbContract.CONTENT_AUTHORITY + "/"
                        + MovieDbContract.PATH;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

}
