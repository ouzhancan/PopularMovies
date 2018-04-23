package com.udacity.popularmovies;

import android.content.res.Configuration;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.udacity.popularmovies.data.MovieDbContract;
import com.udacity.popularmovies.model.Genre;
import com.udacity.popularmovies.model.GenreContainer;
import com.udacity.popularmovies.model.Movie;
import com.udacity.popularmovies.model.MovieContainer;
import com.udacity.popularmovies.utilities.ContentProviderUtil;
import com.udacity.popularmovies.utilities.NetworkUtil;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<MovieContainer>,View.OnClickListener{

    private static final int LOADER_ID = 86;

    RecyclerView mRecyclerView;
    ProgressBar mProgressBar;
    MovieAdapter mMovieAdapter;
    LinearLayout mPagerContainer;
    TextView mErrorMsgTextView,mPageCountTextView;
    ImageButton mFirstPageView,mLastPageView,mPreviousPageView,mNextPageView;

    // default selection is getting popular movies
    String selectedOption = NetworkUtil.POPULAR_MOVIE_PATH;;
    List<Movie> movies = new ArrayList();

    Boolean isLandscapeMode = false;
    int currentPage = 1;
    int totalPage=0;

    public static final String SCREEN_MODE_KEY  = "SCREEN_MODE";
    public static final String SELECTED_OPTION  = "SELECTED_OPTION";

    public static final int NUMBER_OF_RESULT_PER_PAGE = 20;

    ContentProviderUtil cpUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // get device current orientation
        isLandscapeMode = (getResources().getConfiguration().orientation
                == Configuration.ORIENTATION_LANDSCAPE ? true : false);

        if (savedInstanceState != null) {
            isLandscapeMode = savedInstanceState.getBoolean(SCREEN_MODE_KEY);
            selectedOption = savedInstanceState.getString(SELECTED_OPTION);
        }

        getSupportActionBar().setTitle(getString(R.string.app_name));
        getSupportActionBar().setLogo(R.drawable.ic_logo_primary_green);

        // getting xml elements into to android view objects. //
        getViewObjects();

        cpUtil = new ContentProviderUtil(this);


        // grid layout for the recycler view
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);

        // set custom adapter to recycler view
        mMovieAdapter = new MovieAdapter(this, movies,isLandscapeMode,selectedOption);
        mRecyclerView.setAdapter(mMovieAdapter);

        viewMovies();
    }

    public void getViewObjects() {
        mRecyclerView = findViewById(R.id.rv_movie_list);
        mProgressBar = findViewById(R.id.pb_loading_indicator);
        mErrorMsgTextView = findViewById(R.id.tv_error_message);

        mPagerContainer = findViewById(R.id.pager_container);
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
                currentPage = 1;
                selectedOption = NetworkUtil.POPULAR_MOVIE_PATH;
                mMovieAdapter.setSelectedOption(selectedOption);
                getSupportLoaderManager().restartLoader(LOADER_ID, null, this);
                getSupportActionBar().setTitle(getString(R.string.popular_title));
                return true;

            case R.id.menu_sort_top:
                currentPage = 1;
                selectedOption = NetworkUtil.TOP_RATED_MOVIE_PATH;
                mMovieAdapter.setSelectedOption(selectedOption);
                getSupportLoaderManager().restartLoader(LOADER_ID, null, this);
                getSupportActionBar().setTitle(getString(R.string.top_rated_title));
                return true;

            case R.id.menu_sort_upcoming:
                currentPage = 1;
                selectedOption = NetworkUtil.UPCOMING_MOVIE_PATH;
                mMovieAdapter.setSelectedOption(selectedOption);
                getSupportLoaderManager().restartLoader(LOADER_ID, null, this);
                getSupportActionBar().setTitle(getString(R.string.upcoming_title));
                return true;

            case R.id.menu_sort_favorites:
                currentPage = 1;
                selectedOption = NetworkUtil.FAVORITES_MOVIE_PATH;
                mMovieAdapter.setSelectedOption(selectedOption);
                new AsyncTask<Void,Void,Cursor>(){

                    @Override
                    protected void onPreExecute() {
                        super.onPreExecute();
                        showLoadingOnView();
                    }

                    @Override
                    protected Cursor doInBackground(Void... voids) {
                        return cpUtil.getAllFavorites(MovieDbContract.FavoriteEntry.CONTENT_URI);
                        /*
                        return getContentResolver().query(MovieDbContract.FavoriteEntry.CONTENT_URI,
                                null,null,null,null);
                         */
                    }

                    @Override
                    protected void onPostExecute(Cursor cursor) {
                        super.onPostExecute(cursor);

                        showDataOnView();
                        if(cursor != null && cursor.getCount() > 0){
                            // get favorite movies from db and add movies list.
                            addFavoritesToList(cursor);

                            viewMovies();

                            getSupportActionBar().setTitle(getString(R.string.favorite_title));
                        }else{
                            showErrorMessage(getString(R.string.error_no_favorite_movie));
                        }

                    }
                }.execute();

                return true;

            case R.id.menu_settings:
                // not implemented yet. //
                return true;

            case R.id.menu_refresh:

                    viewMovies();

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

                if (movieContainer == null) {
                    showLoadingOnView();
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
            movies = data.getResults();
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
        mMovieAdapter = new MovieAdapter(this, movies,isLandscapeMode,selectedOption);
        mRecyclerView.setAdapter(mMovieAdapter);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putBoolean(SCREEN_MODE_KEY,isLandscapeMode);
        outState.putString(SELECTED_OPTION,selectedOption);

        super.onSaveInstanceState(outState);
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
                currentPage = totalPage-1;
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
            viewMovies();

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
        mPagerContainer.setVisibility(View.VISIBLE);
    }

    private void showLoadingOnView(){
        mProgressBar.setVisibility(View.VISIBLE);
        mErrorMsgTextView.setVisibility(View.INVISIBLE);
        mRecyclerView.setVisibility(View.INVISIBLE);
        mPagerContainer.setVisibility(View.INVISIBLE);
    }

    private void showErrorMessage(String errorMsg) {

        mRecyclerView.setVisibility(View.INVISIBLE);
        mErrorMsgTextView.setVisibility(View.VISIBLE);
        mProgressBar.setVisibility(View.INVISIBLE);
        mPagerContainer.setVisibility(View.INVISIBLE);

        mErrorMsgTextView.setText(errorMsg);
    }

    private void viewMovies(){

        // if selectedOption is not favorite, it will get movies through the network
        if(selectedOption != NetworkUtil.FAVORITES_MOVIE_PATH){
            viewMoviesThroughNetwork();
        }else {
            viewFavoritesFromDb();
        }
    }

    private void viewMoviesThroughNetwork(){
        if (NetworkUtil.isThereAConnection(this)) {
            if (!loaderWasStarted()) {
                getSupportLoaderManager().initLoader(LOADER_ID, null, this);
            } else {
                getSupportLoaderManager().restartLoader(LOADER_ID, null, this);
            }
        } else {
            showErrorMessage(getString(R.string.connection_error_message));
        }
    }

    private void viewFavoritesFromDb(){

        if(movies != null && movies.size()>0){
            int mPage = currentPage;
            List<Movie> subList;

            if((NUMBER_OF_RESULT_PER_PAGE * mPage) > movies.size() ){
                subList = movies.subList(NUMBER_OF_RESULT_PER_PAGE*(mPage-1),movies.size());
            }else{
                subList = movies.subList(NUMBER_OF_RESULT_PER_PAGE*(mPage-1),NUMBER_OF_RESULT_PER_PAGE*mPage );
            }

            MovieContainer dbContainer = new MovieContainer(currentPage,movies.size(),subList);
            mMovieAdapter.setData(dbContainer);
        }else{

            showErrorMessage(getString(R.string.error_no_favorite_movie));
        }
    }

    private void addFavoritesToList(Cursor cursor){

        if(movies != null && movies.size() > 0){
            movies.clear();
        }

        if(cursor != null && cursor.getCount() > 0){

            if(!cursor.isFirst()){
                cursor.moveToFirst();
            }
            try {
                do {
                    Movie newMovie = new Movie();
                    newMovie.setId(cursor.getString(cursor.getColumnIndex(
                            MovieDbContract.FavoriteEntry.COLUMN_MOVIE_ID)));

                    newMovie.setTitle(cursor.getString(cursor.getColumnIndex(
                            MovieDbContract.FavoriteEntry.COLUMN_MOVIE_TITLE)));

                    newMovie.setGenre_ids(stringToGenres(cursor.getString(cursor.getColumnIndex(
                            MovieDbContract.FavoriteEntry.COLUMN_MOVIE_GENRES))));

                    newMovie.setTagline(cursor.getString(cursor.getColumnIndex(
                            MovieDbContract.FavoriteEntry.COLUMN_MOVIE_TAGLINE)));

                    newMovie.setVote_average(Double.valueOf(cursor.getString(cursor.getColumnIndex(
                            MovieDbContract.FavoriteEntry.COLUMN_MOVIE_VOTE))));

                    newMovie.setRelease_date(cursor.getString(cursor.getColumnIndex(
                            MovieDbContract.FavoriteEntry.COLUMN_MOVIE_RELEASE_DATE)));

                    newMovie.setHomepage(cursor.getString(cursor.getColumnIndex(
                            MovieDbContract.FavoriteEntry.COLUMN_MOVIE_HOMEPAGE)));

                    newMovie.setImdb_id(cursor.getString(cursor.getColumnIndex(
                            MovieDbContract.FavoriteEntry.COLUMN_MOVIE_IMDB)));

                    newMovie.setOverview(cursor.getString(cursor.getColumnIndex(
                            MovieDbContract.FavoriteEntry.COLUMN_MOVIE_OVERVIEW)));

                    newMovie.setPoster_path(cursor.getString(cursor.getColumnIndex(
                            MovieDbContract.FavoriteEntry.COLUMN_POSTER_PATH)));

                    newMovie.setBackdrop_path(cursor.getString(cursor.getColumnIndex(
                            MovieDbContract.FavoriteEntry.COLUMN_BACKDROP_PATH)));

                    movies.add(newMovie);

                }while (cursor.moveToNext());
            } finally {
                cursor.close();
            }
        }
    }

    @NonNull
    public static List<String> stringToGenres(String genres){

        List<String> list=new ArrayList();

        for(String element : genres.split(" ")){
            Genre genre = GenreContainer.findId(element);
            if(genre != null ){
                list.add(String.valueOf(genre.getId()));
            }
        }

        return list;
    }


}
