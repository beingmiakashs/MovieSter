package com.omeletlab.sa.moviester.fragment;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

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
public class MovieListingFragment extends ProgressFragment {

    private final List<Movie> mMovieList = new ArrayList<>();
    private JSONArray moviesJsonArray;

    public MovieListingAdapter movieListingAdapter;
    private String appFeature;
    private String movieID;
    private int pageCount = 1;
    Map<String, Boolean> uniqueMovieMap = new HashMap<String, Boolean>();

    public MovieListingFragment() {
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setContentShown(false);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.movie_list_recycle_view, container, false);
        setRetainInstance(true);

        appFeature = getArguments().getString(GlobalConstant.APP_FEATURE);
        movieID = getArguments().getString(GlobalConstant.TAG_id);

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
                pageCount++;
                loadMoviesList();
            }
        };
        recyclerView.setAdapter(movieListingAdapter);


        mMovieList.clear();
        movieListingAdapter.notifyDataSetChanged();

        Network netwrok = new Network(getActivity());
        if (netwrok.isNetworkConnected()) {
            loadMoviesList();
        } else {
            GlobalConstant.showMessage(getActivity(), "Internet Conntection is not available.");
        }

        return view;
    }

    public void loadMoviesList() {
        if(pageCount>100) return;

        String fullUrl = getFullApiURL();
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                fullUrl, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {

                try {
                    moviesJsonArray = response.getJSONArray("results");
                    MovieListingFragment.this.getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                for (int i = 0; i < moviesJsonArray.length(); i++) {
                                    JSONObject item = moviesJsonArray.getJSONObject(i);

                                    String id = item.getString(GlobalConstant.TAG_id);
                                    String backdropPath = item.getString(GlobalConstant.TAG_backdrop_path);
                                    String title = item.getString(GlobalConstant.TAG_title);
                                    String origionalTitle = item.getString(GlobalConstant.TAG_original_title);
                                    String releaseDateString = item.getString(GlobalConstant.TAG_release_date);
                                    String overview = item.getString(GlobalConstant.TAG_overview);
                                    String origionalLanguage = item.getString(GlobalConstant.TAG_original_language);
                                    String posterPath = item.getString(GlobalConstant.TAG_poster_path);
                                    double popularity = item.getDouble(GlobalConstant.TAG_popularity);
                                    double voteAverage = item.getDouble(GlobalConstant.TAG_vote_average);
                                    double voteCount = item.getDouble(GlobalConstant.TAG_vote_count);

                                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                                    Date releaseDate = new Date();
                                    try {
                                        releaseDate = dateFormat.parse(releaseDateString);
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                    if (!uniqueMovieMap.containsKey(id)) {
                                        mMovieList.add(new Movie(id, backdropPath, title, origionalTitle, origionalLanguage, overview, releaseDate, posterPath, popularity, voteAverage, voteCount));
                                        movieListingAdapter.notifyItemInserted(i);
                                    }
                                    else{
                                        uniqueMovieMap.put(id,true);
                                    }
                                }
                                setContentShown(true);
                                Collections.sort(mMovieList, new MovieComparator());
                                movieListingAdapter.notifyDataSetChanged();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("Volley library", "Error: " + error.getMessage());
            }
        });

        AppController.getInstance().addToRequestQueue(jsonObjReq);
    }

    private class MovieComparator implements Comparator<Movie> {

        @Override
        public int compare(Movie m1, Movie m2) {
            return (m1.getPopularity() < m2.getPopularity()) ? 1 : (m1.getPopularity() > m2.getPopularity() ? -1 : 0);
        }
    }

    private String getFullApiURL(){
        String fullURL;
        if(!TextUtils.isEmpty(movieID)) {
            fullURL = GlobalConstant.API_URL+movieID+"/"+ appFeature + "?api_key=" + GlobalConstant.API_KEY + "&page=" + pageCount;
        }
        else{
            fullURL = GlobalConstant.API_URL + appFeature + "?api_key=" + GlobalConstant.API_KEY + "&page=" + pageCount;
        }
        return fullURL;
    }
}
