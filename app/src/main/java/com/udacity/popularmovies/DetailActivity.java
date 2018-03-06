package com.udacity.popularmovies;

import android.app.LoaderManager;
import android.content.AsyncTaskLoader;
import android.content.Intent;
import android.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.udacity.popularmovies.model.Movie;
import com.udacity.popularmovies.utilities.NetworkUtil;

public class DetailActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Movie> {

    String movie_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Intent receivedIntent = getIntent();

        if(receivedIntent != null){
            movie_id = receivedIntent.getExtras().getString("movie_id");
        }

        if( movie_id != null ){
            getSupportActionBar().setTitle(getString(R.string.app_name));
            getSupportActionBar().setLogo(R.drawable.ic_logo_primary_green);
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            getViewObjects();

        }else{
            showErrorMessage("hata");
        }

    }

    public void getViewObjects() {
    }

    @Override
    public Loader<Movie> onCreateLoader(int id, Bundle args) {
        return new AsyncTaskLoader<Movie>(this) {
            @Override
            public Movie loadInBackground() {
                return NetworkUtil.getMoviDetailById(movie_id);
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<Movie> loader, Movie data) {
        if(data != null){
            showMovieDetail();
        }
    }

    @Override
    public void onLoaderReset(Loader<Movie> loader) {

    }


    private void showMovieDetail() {
    }

    private void showErrorMessage(String errorMsg) {
    }

}
