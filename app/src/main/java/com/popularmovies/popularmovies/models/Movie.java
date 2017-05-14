package com.popularmovies.popularmovies.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by blessochampion on 4/17/17.
 */

public class Movie implements Parcelable
{
    private String id;
    private String title;
    private String thumbnailURL;
    private String synopsis;
    private String releaseDate;
    private double userRating;

    public Movie(String id, String title, String thumbnailURL, String synopsis, String releaseDate, double userRating) {
        this.id = id;
        this.title = title;
        this.thumbnailURL = thumbnailURL;
        this.synopsis = synopsis;
        this.releaseDate = releaseDate;
        this.userRating = userRating;
    }


    protected Movie(Parcel in) {
        id = in.readString();
        title = in.readString();
        thumbnailURL = in.readString();
        synopsis = in.readString();
        releaseDate = in.readString();
        userRating = in.readDouble();
    }



    public String getTitle() {
        return title;
    }

    public String getThumbnailURL() {
        return thumbnailURL;
    }

    public String getSynopsis() {
        return synopsis;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public double getUserRating() {
        return userRating;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(title);
        dest.writeString(thumbnailURL);
        dest.writeString(synopsis);
        dest.writeString(releaseDate);
        dest.writeDouble(userRating);
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };


    public String getId() {
        return id;
    }
}
