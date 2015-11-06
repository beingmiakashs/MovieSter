package com.omeletlab.sa.moviester.activity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.omeletlab.sa.moviester.R;
import com.omeletlab.sa.moviester.adapter.ViewPagerAdapter;
import com.omeletlab.sa.moviester.fragment.FavouriteMovieListingFragment;
import com.omeletlab.sa.moviester.fragment.MovieListingFragment;
import com.omeletlab.sa.moviester.util.GlobalConstant;
import com.omeletlab.sa.moviester.util.Network;

/**
 * Created by akashs on 11/5/15.
 */
public class HomeActivity extends AppCompatActivity {

    private View rootView;
    private ViewPager viewPager;
    private Toolbar toolbar;
    private TabLayout tabLayout;

    private Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        rootView = findViewById(R.id.rootView);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        viewPager = (ViewPager) findViewById(R.id.movieViewpager);
        viewPager.setOffscreenPageLimit(5);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        setupTabIcons();

        Network netwrok = new Network(HomeActivity.this);
        if (netwrok.isNetworkConnected()) {
        } else {
            Snackbar.make(rootView, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
        }
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        bundle = new Bundle();
        bundle.putString(GlobalConstant.APP_FEATURE, GlobalConstant.API_FEATURE_NOW_PLAYING);
        Fragment movieListingfragment = new MovieListingFragment();
        movieListingfragment.setArguments(bundle);
        adapter.addFragment(movieListingfragment, getResources().getString(R.string.tab_now_showing));

        bundle = new Bundle();
        Fragment favouriteMovieListingFragment = new FavouriteMovieListingFragment();
        favouriteMovieListingFragment.setArguments(bundle);
        adapter.addFragment(favouriteMovieListingFragment, getResources().getString(R.string.tab_favourites));

        viewPager.setAdapter(adapter);
    }

    private void setupTabIcons() {
        tabLayout.getTabAt(0).setText(getResources().getString(R.string.tab_now_showing));
        tabLayout.getTabAt(1).setText(getResources().getString(R.string.tab_favourites));
    }
}
