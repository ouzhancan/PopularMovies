package com.udacity.popularmovies;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.udacity.popularmovies.model.Movie;
import com.udacity.popularmovies.model.MovieContainer;
import com.udacity.popularmovies.retrofit.APIClient;
import com.udacity.popularmovies.retrofit.APIInterface;
import com.udacity.popularmovies.utilities.NetworkUtil;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<MovieContainer> {

    private static final int LOADER_ID = 86;

    RecyclerView mRecyclerView;
    ProgressBar mProgressBar;
    MovieAdapter mMovieAdapter;
    TextView mErrorMsgTextView;

    String selectedOption;
    List<Movie> movies;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().setTitle(getString(R.string.app_name));
        getSupportActionBar().setLogo(R.drawable.ic_logo_primary_green);

        // getting xml elements into to android view objects. //
        getViewObjects();

        // default selection is getting popular movies
        selectedOption = NetworkUtil.POPULAR_MOVIE_PATH;

        // grid layout for the recycler view
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);

        // set custom adapter to recycler view
        mMovieAdapter = new MovieAdapter(this, movies);
        mRecyclerView.setAdapter(mMovieAdapter);


        if (isThereAConnection()) {
            // get and start loader
            getSupportLoaderManager().initLoader(LOADER_ID, null, this);
        } else {
            // check the movie list was loaded before
            if (!loaderWasStarted()) {
                showErrorMessage(getString(R.string.connection_error_message));
            }else{
                getSupportLoaderManager().restartLoader(LOADER_ID, null, this);
            }
        }
    }

    public void getViewObjects() {
        mRecyclerView = findViewById(R.id.rv_movie_list);
        mProgressBar = findViewById(R.id.pb_loading_indicator);
        mErrorMsgTextView = findViewById(R.id.tv_error_message);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // menu inflation
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // Action Bar öğelerindeki basılmaları idare edelim
        switch (item.getItemId()) {
            case R.id.menu_sort_popular:
                selectedOption = NetworkUtil.POPULAR_MOVIE_PATH;
                getSupportLoaderManager().restartLoader(LOADER_ID, null, this);
                return true;
            case R.id.menu_sort_top:
                selectedOption = NetworkUtil.TOP_RATED_MOVIE_PATH;
                getSupportLoaderManager().restartLoader(LOADER_ID, null, this);
                return true;
            case R.id.menu_settings:
                // not implemented yet. //
                return true;
            case R.id.menu_refresh:

                if (isThereAConnection()) {
                    if (!loaderWasStarted()) {
                        getSupportLoaderManager().initLoader(LOADER_ID, null, this);
                    } else {
                        getSupportLoaderManager().restartLoader(LOADER_ID, null, this);
                    }
                } else {
                    showErrorMessage(getString(R.string.connection_error_message));
                }
                return true;
            default:
                selectedOption = NetworkUtil.POPULAR_MOVIE_PATH;
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public Loader<MovieContainer> onCreateLoader(int id, Bundle args) {
        return new AsyncTaskLoader<MovieContainer>(this) {

            MovieContainer movieContainer;

            @Override
            protected void onStartLoading() {
                super.onStartLoading();

                if (movieContainer == null) {
                    mProgressBar.setVisibility(View.VISIBLE);
                    forceLoad();
                } else {
                    deliverResult(movieContainer);
                }
            }

            @Override
            public MovieContainer loadInBackground() {
                // get movies according to selected option
                return NetworkUtil.getMoviesWithAPI(selectedOption);

            }

            @Override
            public void deliverResult(MovieContainer data) {

                //mMovieAdapter.setData(data);

                super.deliverResult(data);
            }
        };
    }



    @Override
    public void onLoadFinished(Loader<MovieContainer> loader, MovieContainer data) {
        if (data != null) {
            showDataOnView();
            mMovieAdapter.setData(data);
        } else {
            showErrorMessage(getString(R.string.error_message));
        }
    }

    @Override
    public void onLoaderReset(Loader<MovieContainer> loader) {

    }

    /**
     * Check internet connection
     *
     * @return
     */
    public boolean isThereAConnection() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    /**
     * if the loader was started, it doesn't need to start again.
     *
     * @return
     */
    private boolean loaderWasStarted() {

        if (getSupportLoaderManager().getLoader(LOADER_ID) != null &&
                getSupportLoaderManager().getLoader(LOADER_ID).isStarted()) {
            return true;
        } else {
            return false;
        }
    }

    private void showDataOnView() {
        mProgressBar.setVisibility(View.INVISIBLE);
        mErrorMsgTextView.setVisibility(View.INVISIBLE);
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    private void showErrorMessage(String errorMsg) {

        mRecyclerView.setVisibility(View.INVISIBLE);
        mErrorMsgTextView.setVisibility(View.VISIBLE);
        mProgressBar.setVisibility(View.INVISIBLE);

        mErrorMsgTextView.setText(errorMsg);
    }
}
