package com.udacity.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.udacity.popularmovies.model.Video;
import com.udacity.popularmovies.model.VideoContainer;

import java.util.List;


public class TrailerAdapter extends ArrayAdapter<Video> {

    Context context;
    List<Video> videoList;
    int layoutResource;
    VideoContainer videoContainer;

    public TrailerAdapter(@NonNull Context context, int resource, @NonNull List<Video> videoList) {
        super(context, resource, videoList);
        this.context = context;
        this.layoutResource = resource;
        this.videoList = videoList;
    }

    private static class ViewHolder {
        TextView name;
        TextView key;
        ImageView play;
        String url;
    }

    @Override
    public int getCount() {
        return videoList==null ? 0 : videoList.size();
    }

    @Override
    public Video getItem(int position) {
        return videoList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        Video mVideo = getItem(position);

        final ViewHolder viewHolder;

        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(layoutResource, parent, false);
            viewHolder.name = convertView.findViewById(R.id.tv_trailer_name);
            viewHolder.play = convertView.findViewById(R.id.iv_trailer);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.name.setText(mVideo.name);
        viewHolder.url = mVideo.key;


        viewHolder.play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context,"Clicked trailer : "+viewHolder.url
                        ,Toast.LENGTH_LONG).show();

                Intent intent = new Intent(context,PlayVideoActivity.class);
                context.startActivity(intent);

            }
        });

        return convertView;
    }


    public void setData(VideoContainer data) {
        videoContainer = data;

        if (videoContainer != null) {
            this.videoList = videoContainer.getResults();
            notifyDataSetChanged();
        }
    }
}
