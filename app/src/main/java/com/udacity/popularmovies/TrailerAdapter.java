package com.udacity.popularmovies;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.udacity.popularmovies.model.Video;

import java.util.List;


public class TrailerAdapter extends BaseAdapter {

    Context context;
    List<Video> videoList;

    public TrailerAdapter(Context context, List<Video> videoList) {
        this.context = context;
        this.videoList = videoList;
    }

    @Override
    public int getCount() {
        return videoList.size();
    }

    @Override
    public Object getItem(int position) {
        return videoList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        ImageView mPlayButton;
        TextView mTrailerName;

        View view = LayoutInflater.from(context)
                .inflate(R.layout.trailer_row_layout,parent);


        mPlayButton = view.findViewById(R.id.iv_trailer);
        mTrailerName = view.findViewById(R.id.tv_trailer_name);

        mPlayButton.setBackgroundColor(Color.GREEN);
        mTrailerName.setText(videoList.get(position).getName());

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context,"Clicked trailer : "+videoList.get(position).getKey()
                        ,Toast.LENGTH_LONG).show();
            }
        });


        return view;
    }
}
