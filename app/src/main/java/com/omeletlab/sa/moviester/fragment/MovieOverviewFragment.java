package com.omeletlab.sa.moviester.fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.omeletlab.sa.moviester.R;
import com.omeletlab.sa.moviester.util.AppController;
import com.omeletlab.sa.moviester.util.GlobalConstant;

/**
 * Created by akashs on 11/5/15.
 */
public class MovieOverviewFragment extends Fragment {

    private String movieOverview;
    private String movieTitle;
    private String posterPath;
    private TextView movieTitleTV;
    private TextView overviewTV;
    private NetworkImageView posterView;
    private ImageLoader imageLoader = AppController.getInstance().getImageLoader();

    public MovieOverviewFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.movie_overview, container, false);
        setRetainInstance(true);

        if (imageLoader == null) {
            imageLoader = AppController.getInstance().getImageLoader();
        }

        movieTitle = getArguments().getString(GlobalConstant.TAG_title);
        movieOverview = getArguments().getString(GlobalConstant.TAG_overview);
        posterPath = getArguments().getString(GlobalConstant.TAG_poster_path);

        Context context = getActivity();
        overviewTV = (TextView) view.findViewById(R.id.overview);
        movieTitleTV = (TextView) view.findViewById(R.id.movieTitle);
        posterView = (NetworkImageView) view.findViewById(R.id.poster);

        posterView.setImageUrl(GlobalConstant.API_IMAGE_BASE_URL +posterPath, imageLoader);
        overviewTV.setText(movieOverview);
        movieTitleTV.setText(movieTitle);

        return view;
    }
}
