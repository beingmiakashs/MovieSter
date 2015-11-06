package com.omeletlab.sa.moviester.adapter;

import android.app.Activity;
import android.content.Intent;
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
import com.omeletlab.sa.moviester.util.AppController;
import com.omeletlab.sa.moviester.util.GlobalConstant;

import java.util.List;

/**
 * Created by akashs on 11/5/15.
 */
public abstract class MovieListingAdapter extends RecyclerView.Adapter<MovieListingAdapter.MovieViewHolder> {
    List<Movie> movieList;
    Activity mActivity;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();

    private int lastPosition = -1;

    public MovieListingAdapter(List<Movie> movieList, Activity activity) {
        this.movieList = movieList;
        this.mActivity = activity;
    }

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_card, parent, false);
        MovieViewHolder pvh = new MovieViewHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(MovieViewHolder holder, final int position) {

        if(position>= getItemCount()-1){
            loadMore();
        }

        Movie movie = movieList.get(position);
        if (imageLoader == null) {
            imageLoader = AppController.getInstance().getImageLoader();
        }
        holder.poster.setImageUrl(GlobalConstant.API_IMAGE_BASE_URL + movie.getPosterPath(), imageLoader);

        holder.cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mActivity, MovieDetailsActivity.class);
                intent.putExtra(GlobalConstant.TAG_MOVIE_DETAILS, movieList.get(position));
                mActivity.startActivity(intent);
            }
        });
        setAnimation(holder.cv, position);
    }

    @Override
    public int getItemCount() {
        if (movieList != null) {
            return movieList.size();
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

    public abstract void loadMore();

}
