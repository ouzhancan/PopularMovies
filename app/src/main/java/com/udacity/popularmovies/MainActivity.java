package com.udacity.popularmovies;

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
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

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

    String selectedOption;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // getting xml elements into to android view objects. //
        getViewObjects();

        // grid layout for the recycler view
        GridLayoutManager layoutManager = new GridLayoutManager(this,2);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);

        // set custom adapter to recycler view
        mMovieAdapter = new MovieAdapter(this);
        mRecyclerView.setAdapter(mMovieAdapter);

        // get loader
        getSupportLoaderManager().initLoader(LOADER_ID, null, this);

    }

    public void getViewObjects(){
        mRecyclerView = findViewById(R.id.rv_movie_list);
        mProgressBar = findViewById(R.id.pb_loading_indicator);
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
            case R.id.group_item1:
                selectedOption = NetworkUtil.POPULAR_MOVIE_PATH;
                getSupportLoaderManager().restartLoader(LOADER_ID,null,this);
                return true;
            case R.id.group_item2:
                selectedOption = NetworkUtil.TOP_RATED_MOVIE_PATH;
                getSupportLoaderManager().restartLoader(LOADER_ID,null,this);
                return true;
            default:
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

                    if(movieContainer == null) {
                        mProgressBar.setVisibility(View.VISIBLE);
                        forceLoad();
                    }
                    else{
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
                movieContainer = data;
                super.deliverResult(data);
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<MovieContainer> loader, MovieContainer data) {

        mProgressBar.setVisibility(View.INVISIBLE);
        if (data != null) {
            showDataOnView();
            mMovieAdapter.setData(data);
        } else {
            showErrorMessage();
        }
    }

    @Override
    public void onLoaderReset(Loader<MovieContainer> loader) {

    }

    private void showDataOnView() {
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    private void showErrorMessage() {

        mRecyclerView.setVisibility(View.INVISIBLE);

        // mErrorMessageDisplay.setVisibility(View.VISIBLE);
    }
}
