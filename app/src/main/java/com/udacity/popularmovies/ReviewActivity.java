package com.udacity.popularmovies;

import android.content.Intent;
import android.os.Parcelable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ListView;

import com.udacity.popularmovies.model.Review;

import java.util.ArrayList;
import java.util.List;

public class ReviewActivity extends FragmentActivity {

    ListView mListView;

    String movieId,movieName;
    ArrayList<Review> reviewList = new ArrayList();
    ReviewAdapter reviewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);

        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {
            movieId = bundle.getString("movie_id");
            movieName = bundle.getString("movie_name");
            reviewList = (ArrayList<Review>) bundle.getSerializable("reviews");
        }

        getViewObjects();

        reviewAdapter = new ReviewAdapter(this,R.layout.review_row_layout,reviewList);
        mListView.setAdapter(reviewAdapter);
        reviewAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId()){
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString("movie_id",movieId);
        outState.putString("movie_name",movieName);
        outState.putSerializable("reviews",reviewList);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onBackPressed() {
        Intent upIntent = new Intent();
        upIntent.putExtra("movie_id",movieId);
        NavUtils.navigateUpTo(ReviewActivity.this,upIntent);
        super.onBackPressed();
    }

    private void getViewObjects() {
        mListView = findViewById(R.id.lv_more_review);
    }
}
