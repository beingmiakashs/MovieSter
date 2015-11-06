package com.omeletlab.sa.moviester.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.omeletlab.sa.moviester.R;
import com.omeletlab.sa.moviester.adapter.ViewPagerAdapter;
import com.omeletlab.sa.moviester.fragment.MovieOverviewFragment;
import com.omeletlab.sa.moviester.fragment.MovieListingFragment;
import com.omeletlab.sa.moviester.fragment.TrailerListingFragment;
import com.omeletlab.sa.moviester.model.Movie;
import com.omeletlab.sa.moviester.util.AppController;
import com.omeletlab.sa.moviester.util.GlobalConstant;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by akashs on 11/5/15.
 */
public class MovieDetailsActivity extends AppCompatActivity {

    private Movie movie;
    NetworkImageView backdropView;
    private ImageLoader imageLoader = AppController.getInstance().getImageLoader();
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (imageLoader == null) {
            imageLoader = AppController.getInstance().getImageLoader();
        }

        Intent intent = getIntent();
        movie = (Movie) intent.getSerializableExtra(GlobalConstant.TAG_MOVIE_DETAILS);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy");
        String releaseyear = dateFormat.format(movie.getReleaseDate());
        getSupportActionBar().setTitle(movie.getTitle()+"("+releaseyear+")");

        backdropView = (NetworkImageView) findViewById(R.id.backdrop);

        if(movie!=null){
            backdropView.setImageUrl(GlobalConstant.API_IMAGE_BASE_URL + movie.getBackdropPath(), imageLoader);
        }

        viewPager = (ViewPager) findViewById(R.id.movieViewpager);
        viewPager.setOffscreenPageLimit(5);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        setupTabIcons();



        FloatingActionButton markFavouritesBtn = (FloatingActionButton) findViewById(R.id.markFavouritesBtn);
        markFavouritesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<Movie> checkItem = Movie.find(Movie.class, "movie_id = ?", movie.getMovieId());
                if (checkItem.isEmpty()) {
                    movie.save();
                }
                Snackbar.make(view, getResources().getString(R.string.add_favourite_movie_message), Snackbar.LENGTH_LONG).show();
            }
        });
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        bundle = new Bundle();
        bundle.putString(GlobalConstant.TAG_overview, movie.getOverview());
        bundle.putString(GlobalConstant.TAG_title, movie.getTitle());
        bundle.putString(GlobalConstant.TAG_poster_path, movie.getPosterPath());
        Fragment movieDetailsfragment = new MovieOverviewFragment();
        movieDetailsfragment.setArguments(bundle);
        adapter.addFragment(movieDetailsfragment, getResources().getString(R.string.tab_overview));

        bundle = new Bundle();
        bundle.putString(GlobalConstant.TAG_id, movie.getMovieId());
        Fragment movieTrailersListFragment = new TrailerListingFragment();
        movieTrailersListFragment.setArguments(bundle);
        adapter.addFragment(movieTrailersListFragment, getResources().getString(R.string.tab_videos));

        bundle = new Bundle();
        bundle.putString(GlobalConstant.APP_FEATURE, GlobalConstant.API_FEATURE_SIMILAR);
        bundle.putString(GlobalConstant.TAG_id, movie.getMovieId());
        Fragment similarMovieListFragment = new MovieListingFragment();
        similarMovieListFragment.setArguments(bundle);
        adapter.addFragment(similarMovieListFragment, getResources().getString(R.string.tab_similar));

        viewPager.setAdapter(adapter);
    }

    private void setupTabIcons() {
        tabLayout.getTabAt(0).setText(getResources().getString(R.string.tab_overview));
        tabLayout.getTabAt(1).setText(getResources().getString(R.string.tab_videos));
        tabLayout.getTabAt(2).setText(getResources().getString(R.string.tab_similar));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}
