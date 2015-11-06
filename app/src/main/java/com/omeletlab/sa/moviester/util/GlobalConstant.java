package com.omeletlab.sa.moviester.util;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

import java.util.Calendar;
import java.util.List;

/**
 * Created by akashs on 11/5/15.
 */
public class GlobalConstant {

    public static Context mContext;

    public static final String API_URL = "http://api.themoviedb.org/3/movie/";
    public static final String API_KEY = "930750c3c053a1ec4573788ae5dd4e0d";
    public static final String API_IMAGE_BASE_URL = "http://image.tmdb.org/t/p/w500";
    public static final String API_IMAGE_YOUTUBE_URL = "http://img.youtube.com/vi/";

    public static final String API_FEATURE_NOW_PLAYING = "now_playing";
    public static final String API_FEATURE_SIMILAR = "similar";
    public static final String API_FEATURE_FAVOURITES = "favourites";
    public static final String APP_FEATURE = "feature";

    public static final String TAG_id = "id";
    public static final String TAG_backdrop_path = "backdrop_path";
    public static final String TAG_original_language = "original_language";
    public static final String TAG_original_title = "original_title";
    public static final String TAG_title = "title";
    public static final String TAG_overview = "overview";
    public static final String TAG_release_date = "release_date";
    public static final String TAG_poster_path = "poster_path";
    public static final String TAG_popularity = "popularity";
    public static final String TAG_vote_average = "vote_average";
    public static final String TAG_vote_count = "vote_count";

    public static final String TAG_key = "key";
    public static final String TAG_name = "name";
    public static final String TAG_site = "site";
    public static final String TAG_type = "type";


    public static final String TAG_MOVIE_DETAILS = "MOVIE_DETAILS";
    public static final String SITE_YOUTUBE = "YouTube";

    public static final String TAG_YES = "YES";
    public static final String TAG_NO = "NO";
    public static final String TAG_RELOAD = "RELOAD";

    public static void showMessage(final Context context, final String message) {
        ((Activity) context).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(context, message, Toast.LENGTH_LONG).show();
            }
        });
    }
}
