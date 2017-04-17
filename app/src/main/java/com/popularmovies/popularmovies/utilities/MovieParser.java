package com.popularmovies.popularmovies.utilities;

import android.util.Log;

import com.popularmovies.popularmovies.models.Movie;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by blessochampion on 4/17/17.
 */

public class MovieParser
{
    private static final String TAG = MovieParser.class.getSimpleName();
    private static final String KEY_TITLE = "original_title";
    private static final String KEY_THUMBNAIL_URL = "poster_path";
    private static final String KEY_SYNOPSIS = "overview";
    private static final String KEY_RELEASE_DATE = "release_date";
    private static final String KEY_USER_RATING = "vote_average";

    public static  Movie parserMovie(JSONObject movieJSONObject){
        Movie  movie = null;
        try {
            String title = movieJSONObject.getString(KEY_TITLE);
            String thumbnailURL = movieJSONObject.getString(KEY_THUMBNAIL_URL);
            String synopsis = movieJSONObject.getString(KEY_SYNOPSIS);
            String releaseDate = movieJSONObject.getString(KEY_RELEASE_DATE);
            double userRating = movieJSONObject.getDouble(KEY_USER_RATING);

            movie =  new Movie(title, thumbnailURL, synopsis, releaseDate, userRating);
        }catch (JSONException e){
            Log.e(TAG, e.getMessage());
        }

        return movie;
    }
}
