package com.omeletlab.sa.moviester.fragment;

import android.content.Context;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.devspark.progressfragment.ProgressFragment;
import com.omeletlab.sa.moviester.R;
import com.omeletlab.sa.moviester.adapter.MovieListingAdapter;
import com.omeletlab.sa.moviester.model.Movie;
import com.omeletlab.sa.moviester.util.AppController;
import com.omeletlab.sa.moviester.util.GlobalConstant;
import com.omeletlab.sa.moviester.util.Network;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by akashs on 11/5/15.
 */
public class FavouriteMovieListingFragment extends Fragment {

    private final List<Movie> mMovieList = new ArrayList<>();
    public MovieListingAdapter movieListingAdapter;
    private LoadFavouriteMovieList loadFavouriteMovieList;
    private Network netwrok;

    public FavouriteMovieListingFragment() {
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.movie_list_recycle_view, container, false);
        setRetainInstance(true);

        Context context = getActivity();
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);

        if(getActivity().getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
            recyclerView.setLayoutManager(new GridLayoutManager(context, 3));
        }
        else{
            recyclerView.setLayoutManager(new GridLayoutManager(context, 5));
        }

        movieListingAdapter = new MovieListingAdapter(mMovieList, getActivity()){
            @Override
            public void loadMore() {
            }
        };
        recyclerView.setAdapter(movieListingAdapter);


        mMovieList.clear();
        movieListingAdapter.notifyDataSetChanged();

        netwrok = new Network(getActivity());
        if (netwrok.isNetworkConnected()) {
            Log.d("favcall","first");
            loadFavouriteMovieList = new LoadFavouriteMovieList();
            loadFavouriteMovieList.execute();
        } else {
            GlobalConstant.showMessage(getActivity(), "Internet Conntection is not available.");
        }

        return view;
    }

    private class MovieComparator implements Comparator<Movie> {

        @Override
        public int compare(Movie m1, Movie m2) {
            return (m1.getPopularity() < m2.getPopularity()) ? 1 : (m1.getPopularity() > m2.getPopularity() ? -1 : 0);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (netwrok.isNetworkConnected()) {
            Log.d("favcall","second");
            loadFavouriteMovieList = new LoadFavouriteMovieList();
            loadFavouriteMovieList.execute();
        }
    }

    class LoadFavouriteMovieList extends AsyncTask<String, String, String>{


        @Override
        protected String doInBackground(String... strings) {
            List<Movie> movieList = Movie.listAll(Movie.class);
            int i=0;

            mMovieList.clear();
            for (Movie item : movieList) {
                mMovieList.add(item);
                movieListingAdapter.notifyItemInserted(i);
                i++;
            }
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Collections.sort(mMovieList, new MovieComparator());
                    movieListingAdapter.notifyDataSetChanged();
                }
            });
            return "Complete";
        }

        @Override
        protected void onPostExecute(String s) {
        }
    }
}
