package com.udacity.popularmovies;

import android.content.Context;
import android.content.res.Configuration;
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
import android.widget.ImageButton;
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

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<MovieContainer>,View.OnClickListener{

    private static final int LOADER_ID = 86;

    RecyclerView mRecyclerView;
    ProgressBar mProgressBar;
    MovieAdapter mMovieAdapter;
    TextView mErrorMsgTextView,mPageCountTextView;
    ImageButton mFirstPageView,mLastPageView,mPreviousPageView,mNextPageView;

    String selectedOption;
    List<Movie> movies;

    Boolean isLandscapeMode = false;
    int currentPage = 1;
    int totalPage=0;

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
        mMovieAdapter = new MovieAdapter(this, movies,isLandscapeMode);
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

        mPageCountTextView = findViewById(R.id.tv_main_pagination_counter);

        mFirstPageView = findViewById(R.id.iv_first_page);
        mLastPageView = findViewById(R.id.iv_last_page);
        mPreviousPageView = findViewById(R.id.iv_previous_page);
        mNextPageView = findViewById(R.id.iv_next_page);

        mFirstPageView.setOnClickListener(this);
        mLastPageView.setOnClickListener(this);
        mPreviousPageView.setOnClickListener(this);
        mNextPageView.setOnClickListener(this);
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
                currentPage = 1;
                getSupportLoaderManager().restartLoader(LOADER_ID, null, this);
                return true;
            case R.id.menu_sort_top:
                currentPage = 1;
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
                return NetworkUtil.getMoviesWithAPI(selectedOption,String.valueOf(currentPage));

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
            currentPage = data.getPage();
            totalPage = data.getTotal_pages();
            mPageCountTextView.setText("Current Page : "+data.getPage()
                    +"  Total Page : "+data.getTotal_pages());
            mMovieAdapter.setData(data);
        } else {
            showErrorMessage(getString(R.string.error_message));
        }
    }

    @Override
    public void onLoaderReset(Loader<MovieContainer> loader) {

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        //Update the Flag here
        isLandscapeMode = (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE ? true : false);
    }

    /**
     * Pagination buttons onclick
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.iv_first_page:
                currentPage = 1;
                break;
            case R.id.iv_last_page:
                // if release date is null, retrofit is thrown an parse exception
                // I couldn't find a solution :)
                currentPage = totalPage-90;
                break;
            case R.id.iv_previous_page:
                if(currentPage > 1 ) {
                    currentPage--;
                }
                break;
            case R.id.iv_next_page:
                if(currentPage < totalPage){
                    currentPage++;
                }
                break;
        }

        if (isThereAConnection()) {
            if (!loaderWasStarted()) {
                getSupportLoaderManager().initLoader(LOADER_ID, null, this);
            } else {
                getSupportLoaderManager().restartLoader(LOADER_ID, null, this);
            }
        } else {
            showErrorMessage(getString(R.string.connection_error_message));
        }

    }

    /**
     * Check internet connection
     *
     * @return
     */
    private boolean isThereAConnection() {
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
