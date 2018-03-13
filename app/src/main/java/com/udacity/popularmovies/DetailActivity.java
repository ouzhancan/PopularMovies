package com.udacity.popularmovies;

import android.support.constraint.ConstraintLayout;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.content.Context;
import android.support.v4.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.udacity.popularmovies.model.Movie;
import com.udacity.popularmovies.utilities.NetworkUtil;

public class DetailActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Movie> {


    private static final int LOADER_ID = 640;

    Movie selectedMovie;
    String movie_id;
    String genres;

    // view objects //
    ConstraintLayout mConstraintContainer;
    ProgressBar mProgressBar;
    ImageView mBackdropView, mPosterView;
    TextView mTitleView,mGenresView,mOverviewView,mReleaseDateView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        getViewObjects();

        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {
            movie_id = bundle.getString("movie_id");
            genres = bundle.getString("genres");
        }

        if (movie_id != null) {

            if (isThereAConnection()) {
                // get and start loader
                getSupportLoaderManager().initLoader(LOADER_ID, null, this);
            } else {
                // check the movie list was loaded before
                if (!loaderWasStarted()) {
                    showErrorMessage(getString(R.string.connection_error_message));
                } else {
                    getSupportLoaderManager().restartLoader(LOADER_ID, null, this);
                }
            }

            mProgressBar.setVisibility(View.VISIBLE);

            getSupportActionBar().setLogo(R.drawable.ic_logo_primary_green);
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        } else {
            showErrorMessage(getString(R.string.error_message));
        }

    }

    public void getViewObjects() {

        mConstraintContainer = findViewById(R.id.cl_detail_container);
        mConstraintContainer.setVisibility(View.INVISIBLE);

        mProgressBar = findViewById(R.id.pb_detail);
        mProgressBar.setVisibility(View.VISIBLE);

        mBackdropView = findViewById(R.id.iv_detail_backdrop);
        mPosterView = findViewById(R.id.iv_detail_poster);
        mTitleView = findViewById(R.id.tv_detail_title);
        mGenresView = findViewById(R.id.tv_detail_genres);
        // mTitleView = findViewById(R.id.tv_detail_over);
        // mTitleView = findViewById(R.id.tv_detail_title);
    }

    @Override
    public Loader<Movie> onCreateLoader(int id, Bundle args) {
        return new AsyncTaskLoader<Movie>(this) {

            Movie movie;

            @Override
            protected void onStartLoading() {
                super.onStartLoading();

                if (movie == null) {
                    mProgressBar.setVisibility(View.VISIBLE);
                    forceLoad();
                } else {
                    deliverResult(movie);
                }
            }

            @Override
            public Movie loadInBackground() {
                return NetworkUtil.getMoviDetailById(movie_id);
            }

            @Override
            public void deliverResult(Movie data) {
                super.deliverResult(data);

                if (data == null) {
                    showErrorMessage(getString(R.string.error_message));
                }
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<Movie> loader, Movie data) {
        if (data != null) {
            mProgressBar.setVisibility(View.INVISIBLE);
            mConstraintContainer.setVisibility(View.VISIBLE);
            selectedMovie = data;
            getSupportActionBar().setTitle(selectedMovie.getTitle());
            showMovieDetail(data);
        }
    }

    @Override
    public void onLoaderReset(Loader<Movie> loader) {
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


    private void showMovieDetail(Movie movie) {
        if (movie != null) {

            mProgressBar.setVisibility(View.INVISIBLE);
            mConstraintContainer.setVisibility(View.VISIBLE);

            Picasso.with(this)
                    .load(NetworkUtil.BASE_IMAGE_URL + NetworkUtil.IMAGE_BACKDROP_SIZE + movie.getBackdrop_path())
                    .into(mBackdropView);

            Picasso.with(this)
                    .load(NetworkUtil.BASE_IMAGE_URL + NetworkUtil.IMAGE_SIZE + movie.getPoster_path())
                    .into(mPosterView);

            mTitleView.setText(movie.getTitle());
            mGenresView.setText(genres!=null?genres: " -_-_- ");

        }
    }

    private void showErrorMessage(String errorMsg) {

        mConstraintContainer.setVisibility(View.INVISIBLE);
        mProgressBar.setVisibility(View.INVISIBLE);

        Toast toastMessage = Toast.makeText(this,errorMsg,Toast.LENGTH_LONG);
        toastMessage.setGravity(Gravity.CENTER,0,0);
        toastMessage.show();

    }

}
