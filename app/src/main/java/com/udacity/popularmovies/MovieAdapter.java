package com.udacity.popularmovies;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.udacity.popularmovies.model.Movie;
import com.udacity.popularmovies.model.MovieContainer;
import com.udacity.popularmovies.utilities.NetworkUtil;

import java.util.List;

/**
 * Created by ouz on 24/02/18.
 */

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieAdapterViewHolder> {

    Context context;
    MovieContainer movieContainer;
    List<Movie> movieList;

    public MovieAdapter(Context mContext) {
        this.context = mContext;
    }

    @Override
    public MovieAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        //Context mContext = parent.getContext();

        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.movie_card,parent,false);

        MovieAdapterViewHolder viewHolder = new MovieAdapterViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MovieAdapterViewHolder holder, int position) {
        final Movie movie = movieList.get(position);

        holder.movie_title.setText(movie.getTitle());

        Picasso.with(context)
                .load(NetworkUtil.buildImageUrl(movie.getPoster_path()).toString()).into(holder.movie_poster);


        holder.movie_poster.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("CLICK_POSTER",movie.getTitle()+ " POSTER has CLICKED !");
            }
        });
    }

    @Override
    public int getItemCount() {

        return movieList.size();
    }


    public class MovieAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView movie_poster;
        TextView movie_title;

        public MovieAdapterViewHolder(View view) {
            super(view);
            view.setOnClickListener(this);

            movie_poster = view.findViewById(R.id.movie_thumbnail);
            movie_title = view.findViewById(R.id.movie_title);

            movie_poster.setOnClickListener(this);
        }

        /**
         * This gets called by the child views during a click.
         *
         * @param v The View that was clicked
         */
        @Override
        public void onClick(View v) {

            int adapterPosition = getAdapterPosition();

            if(v.getId() == R.id.movie_thumbnail){
                Log.d("CLICK_POSTER"," THUMBNAIL POSTER has CLICKED !");
            }
        }
    }

    public void setData(MovieContainer data) {
        movieContainer = data;
        notifyDataSetChanged();
    }
}
