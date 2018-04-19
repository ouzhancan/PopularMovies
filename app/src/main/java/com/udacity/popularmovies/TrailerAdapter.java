package com.udacity.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.content.MimeTypeFilter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.udacity.popularmovies.model.Video;
import com.udacity.popularmovies.model.VideoContainer;
import com.udacity.popularmovies.utilities.NetworkUtil;

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
        return videoList == null ? 0 : videoList.size();
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


        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("APP_DEBUG","Clicked trailer : " + viewHolder.url);

                startVideo(viewHolder.url);
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


    private void startVideo(String videoID) {
        // default youtube app
        Intent youtubeIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + videoID));
        List<ResolveInfo> list = context.getPackageManager().queryIntentActivities(youtubeIntent,
                PackageManager.MATCH_DEFAULT_ONLY);

        if (list.size() == 0) {
            Intent webIntent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://www.youtube.com/watch?v=" + videoID));
            context.startActivity(webIntent);
        }else{
            context.startActivity(youtubeIntent);
        }
    }
}
