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
import com.omeletlab.sa.moviester.adapter.TrailersAdapter;
import com.omeletlab.sa.moviester.model.MovieVideo;
import com.omeletlab.sa.moviester.util.AppController;
import com.omeletlab.sa.moviester.util.GlobalConstant;
import com.omeletlab.sa.moviester.util.Network;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by akashs on 11/5/15.
 */
public class TrailerListingFragment extends ProgressFragment {

    private final List<MovieVideo> mTrailersList = new ArrayList<>();
    private JSONArray trailersJsonArray;

    public TrailersAdapter rvAdapter;
    private String movieID;

    public TrailerListingFragment() {
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

        movieID = getArguments().getString(GlobalConstant.TAG_id);

        Context context = getActivity();
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);

        if(getActivity().getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
            recyclerView.setLayoutManager(new GridLayoutManager(context, 2));
        }
        else{
            recyclerView.setLayoutManager(new GridLayoutManager(context, 3));
        }

        rvAdapter = new TrailersAdapter(mTrailersList, getActivity());
        recyclerView.setAdapter(rvAdapter);


        mTrailersList.clear();
        rvAdapter.notifyDataSetChanged();

        Network netwrok = new Network(getActivity());
        if (netwrok.isNetworkConnected()) {
            loadMoviesList();
        } else {
            GlobalConstant.showMessage(getActivity(), "Internet Conntection is not available.");
        }

        return view;
    }

    public void loadMoviesList() {

        String fullUrl = getFullApiURL();
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                fullUrl, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {

                try {
                    trailersJsonArray = response.getJSONArray("results");
                    TrailerListingFragment.this.getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                mTrailersList.clear();
                                for (int i = 0; i < trailersJsonArray.length(); i++) {
                                    JSONObject item = trailersJsonArray.getJSONObject(i);

                                    String id = item.getString(GlobalConstant.TAG_id);
                                    String key = item.getString(GlobalConstant.TAG_key);
                                    String name = item.getString(GlobalConstant.TAG_name);
                                    String site = item.getString(GlobalConstant.TAG_site);
                                    String type = item.getString(GlobalConstant.TAG_type);

                                    mTrailersList.add(new MovieVideo(id, key, name, site, type));
                                    rvAdapter.notifyItemInserted(i);
                                }
                                setContentShown(true);
                                rvAdapter.notifyDataSetChanged();
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

    private String getFullApiURL(){
        String fullURL = GlobalConstant.API_URL+movieID+"/videos" + "?api_key=" + GlobalConstant.API_KEY;
        Log.d("fullURL",fullURL);
        return fullURL;
    }
}
