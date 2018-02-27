package com.udacity.popularmovies;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.udacity.popularmovies.model.MovieContainer;
import com.udacity.popularmovies.retrofit.APIClient;
import com.udacity.popularmovies.retrofit.APIInterface;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {


    APIInterface apiService;
    TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv = findViewById(R.id.tv_basic);

        // Retrofit service //
        apiService = APIClient.getClient().create(APIInterface.class);

        Call<MovieContainer> popularMoviesCall = apiService.getPopularMovies(BuildConfig.MOVIE_DB_API_KEY);

        popularMoviesCall.enqueue(new Callback<MovieContainer>() {
            @Override
            public void onResponse(Call<MovieContainer> call, Response<MovieContainer> response) {

                if (response.isSuccessful()) {
                    String result = "";
                    MovieContainer moviePages = response.body();
                    result += String.valueOf(moviePages.getPage()) + "\n";
                    tv.setText(result);

                }
            }

            @Override
            public void onFailure(Call<MovieContainer> call, Throwable t) {
                Log.d("DEMO_LOG", t.getLocalizedMessage());
            }
        });

        // *** *** Retrofit Service *** *** //


    }
}
