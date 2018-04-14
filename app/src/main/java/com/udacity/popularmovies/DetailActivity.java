package com.udacity.popularmovies;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.udacity.popularmovies.data.MovieDbContract;
import com.udacity.popularmovies.data.MovieDbHelper;
import com.udacity.popularmovies.model.Movie;
import com.udacity.popularmovies.utilities.DbUtil;
import com.udacity.popularmovies.utilities.NetworkUtil;

import java.util.Calendar;
import java.util.Date;

public class DetailActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Movie> {


    private static final int LOADER_ID = 640;

    Movie selectedMovie;
    String movie_id;
    String genres;

    // view objects //
    ConstraintLayout mConstraintContainer;
    ProgressBar mProgressBar;
    ImageView mBackdropView, mPosterView;
    TextView mTitleView,mGenresView,mTaglineView,mVoteAverageView;
    TextView mOverviewView,mReleaseDateView,mHomePageView,mImdbLinkView;
    Button mAddRemoveFavorite;

    // db helper
    MovieDbHelper movieDbHelper;
    DbUtil dbUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        getViewObjects();

        movieDbHelper = new MovieDbHelper(this);
        dbUtil = new DbUtil(movieDbHelper);


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
            getSupportActionBar().setTitle(getString(R.string.loading));
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
        mTaglineView = findViewById(R.id.tv_detail_tagline);
        mVoteAverageView = findViewById(R.id.tv_detail_vote);
        mReleaseDateView = findViewById(R.id.tv_detail_release_date);
        mHomePageView = findViewById(R.id.tv_detail_homepage);
        mImdbLinkView = findViewById(R.id.tv_detail_imdb);
        mOverviewView = findViewById(R.id.tv_detail_overview);

        mAddRemoveFavorite = findViewById(R.id.btn_add_remove_favorites);

        // burada favorilerde var mi yok mu once onu kontrol et.
        // eger var ise, butonun seklini semalini degistir.
        // daha sonra butona tiklayinca ne olacagini belirle.
        mAddRemoveFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(selectedMovie != null) {
                    dbUtil.addRemoveFavorite(selectedMovie,genres);
                    invertTheFavoriteButton();
                }
            }
        });
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
                    .load(NetworkUtil.BASE_IMAGE_URL
                            + NetworkUtil.IMAGE_BACKDROP_SIZE
                            + movie.getBackdrop_path())
                    .into(mBackdropView);

            Picasso.with(this)
                    .load(NetworkUtil.BASE_IMAGE_URL
                            + NetworkUtil.IMAGE_SIZE
                            + movie.getPoster_path())
                    .into(mPosterView);

            mTitleView.setText(movie.getTitle());
            mTaglineView.setText(movie.getTagline()!=null?movie.getTagline():" -_-_- ");
            mGenresView.setText(genres!=null?genres: " -_-_- ");
            mVoteAverageView.setText(movie.getVote_average().toString()+"  /  10 "+"    -    "
                                    +movie.getVote_count());

            if(movie.getRelease_date() != "" && movie.getRelease_date().length() > 0
                    && !movie.getRelease_date().isEmpty()){

                String[] datePartitions = movie.getRelease_date().split("-");

                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.YEAR,Integer.valueOf(datePartitions[0]));
                calendar.set(Calendar.MONTH,Integer.valueOf(datePartitions[1]));
                calendar.set(Calendar.DATE,Integer.valueOf(datePartitions[2]));

                mReleaseDateView.setText(String.valueOf(calendar.get(Calendar.DATE))+" / "
                                        +String.valueOf(calendar.get(Calendar.MONTH))+" / "
                                        +String.valueOf(calendar.get(Calendar.YEAR)));


            }
            mHomePageView.setText(movie.getHomepage());
            mImdbLinkView.setText(NetworkUtil.BASE_IMDB_MOVIE_URL + movie.getImdb_id());
            mOverviewView.setText(movie.getOverview());

            // if the movie exists in favorite db,
            String id = dbUtil.getMovieById(movie.getId());
            if(id != "" && id != null){
                invertTheFavoriteButton();
            }
        }
    }

    private void showErrorMessage(String errorMsg) {

        mConstraintContainer.setVisibility(View.INVISIBLE);
        mProgressBar.setVisibility(View.INVISIBLE);

        Toast toastMessage = Toast.makeText(this,errorMsg,Toast.LENGTH_LONG);
        toastMessage.setGravity(Gravity.CENTER,0,0);
        toastMessage.show();

    }



    private void invertTheFavoriteButton(){

        if(mAddRemoveFavorite.getText().equals(getString(R.string.add_favorite))){
            mAddRemoveFavorite.setText(getString(R.string.remove_favorite));
            mAddRemoveFavorite.setBackgroundColor(getResources().getColor(R.color.remove_favorite_color));
        }else{
            mAddRemoveFavorite.setText(getString(R.string.add_favorite));
            mAddRemoveFavorite.setBackgroundColor(getResources().getColor(R.color.add_favorite_color));
        }
    }

}
