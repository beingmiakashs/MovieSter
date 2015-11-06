package com.omeletlab.sa.moviester.model;

import com.orm.SugarRecord;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by akashs on 11/5/15.
 */
public class Movie extends SugarRecord<Movie> implements Serializable {
    private String movieId;
    private String backdropPath;
    private String title;
    private String origionalTitle;
    private String origionalLanguage;
    private String overview;
    private Date releaseDate;
    private String posterPath;
    private double popularity;
    private double voteAverage;
    private double voteCount;


    public Movie(String movieId, String backdropPath, String title, String origionalTitle, String origionalLanguage, String overview, Date releaseDate, String posterPath, double popularity, double voteAverage, double voteCount) {
        this.movieId = movieId;
        this.backdropPath = backdropPath;
        this.title = title;
        this.origionalTitle = origionalTitle;
        this.origionalLanguage = origionalLanguage;
        this.overview = overview;
        this.releaseDate = releaseDate;
        this.posterPath = posterPath;
        this.popularity = popularity;
        this.voteAverage = voteAverage;
        this.voteCount = voteCount;
    }

    public Movie(){}

    public String getMovieId() {
        return movieId;
    }

    public void setMovieId(String movieId) {
        this.movieId = movieId;
    }

    public String getBackdropPath() {
        return backdropPath;
    }

    public void setBackdropPath(String backdropPath) {
        this.backdropPath = backdropPath;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOrigionalTitle() {
        return origionalTitle;
    }

    public void setOrigionalTitle(String origionalTitle) {
        this.origionalTitle = origionalTitle;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public Date getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(Date releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public double getPopularity() {
        return popularity;
    }

    public void setPopularity(double popularity) {
        this.popularity = popularity;
    }

    public double getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(double voteAverage) {
        this.voteAverage = voteAverage;
    }

    public double getVoteCount() {
        return voteCount;
    }

    public void setVoteCount(double voteCount) {
        this.voteCount = voteCount;
    }

    public String getOrigionalLanguage() {
        return origionalLanguage;
    }

    public void setOrigionalLanguage(String origionalLanguage) {
        this.origionalLanguage = origionalLanguage;
    }

}
