package com.omeletlab.sa.moviester.adapter;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.omeletlab.sa.moviester.R;
import com.omeletlab.sa.moviester.activity.MovieDetailsActivity;
import com.omeletlab.sa.moviester.model.Movie;
import com.omeletlab.sa.moviester.model.MovieVideo;
import com.omeletlab.sa.moviester.util.AppController;
import com.omeletlab.sa.moviester.util.GlobalConstant;

import java.util.List;

/**
 * Created by akashs on 11/5/15.
 */
public class TrailersAdapter extends RecyclerView.Adapter<TrailersAdapter.MovieViewHolder> {
    List<MovieVideo> movieVideoList;
    Activity mActivity;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();

    private int lastPosition = -1;

    public TrailersAdapter(List<MovieVideo> movieVideoList, Activity activity) {
        this.movieVideoList = movieVideoList;
        this.mActivity = activity;
    }

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.trailer_card, parent, false);
        MovieViewHolder pvh = new MovieViewHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(MovieViewHolder holder, final int position) {

        MovieVideo movieVideo = movieVideoList.get(position);
        if (imageLoader == null) {
            imageLoader = AppController.getInstance().getImageLoader();
        }
        if(movieVideo.getSite().equals(GlobalConstant.SITE_YOUTUBE)){
            holder.poster.setImageUrl(GlobalConstant.API_IMAGE_YOUTUBE_URL + movieVideo.getKey() + "/0.jpg", imageLoader);
        }

        holder.cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openYoutubeVideo(movieVideoList.get(position).getKey());
            }
        });
        setAnimation(holder.cv, position);
    }

    @Override
    public int getItemCount() {
        if (movieVideoList != null) {
            return movieVideoList.size();
        }
        return 0;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public static class MovieViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        NetworkImageView poster;

        MovieViewHolder(View itemView) {
            super(itemView);
            cv = (CardView) itemView.findViewById(R.id.cv);
            poster = (NetworkImageView) itemView.findViewById(R.id.poster);
        }
    }

    private void setAnimation(View viewToAnimate, int position) {
        if (position > lastPosition) {
            Animation animation = AnimationUtils.loadAnimation(mActivity, android.R.anim.slide_in_left);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }

    private void openYoutubeVideo(String id){
        try{
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + id));
            mActivity.startActivity(intent);
        }catch (ActivityNotFoundException ex){
            Intent intent=new Intent(Intent.ACTION_VIEW,Uri.parse("http://www.youtube.com/watch?v="+id));
            mActivity.startActivity(intent);
        }
    }

}
