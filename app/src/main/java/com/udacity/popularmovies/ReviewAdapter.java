package com.udacity.popularmovies;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.udacity.popularmovies.model.Review;
import com.udacity.popularmovies.model.ReviewContainer;

import java.util.List;

public class ReviewAdapter extends ArrayAdapter<Review> {

    Context context;
    List<Review> reviewList;
    int resourceLayout;
    ReviewContainer reviewContainer;


    public ReviewAdapter(Context context,int resource, List<Review> reviewList) {
        super(context,resource,reviewList);
        this.context = context;
        this.resourceLayout = resource;
        this.reviewList = reviewList;
    }

    private static class ViewHolder {
        TextView author;
        TextView content;
        String url;
    }

    @Override
    public int getCount() {
        return reviewList==null ? 0 : reviewList.size();
    }

    @Override
    public Review getItem(int position) {
        return reviewList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Review mReview = getItem(position);

        final ViewHolder viewHolder;

        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(resourceLayout, parent, false);
            viewHolder.author = convertView.findViewById(R.id.tv_review_author);
            viewHolder.content = convertView.findViewById(R.id.tv_review_content);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.author.setText(mReview.author);
        viewHolder.content.setText(mReview.content);
        viewHolder.url = mReview.url;

        return convertView;
    }

    public void setData(ReviewContainer data) {
        reviewContainer = data;

        if (reviewContainer != null) {
            this.reviewList = reviewContainer.getResults();
            notifyDataSetChanged();
        }
    }
}
