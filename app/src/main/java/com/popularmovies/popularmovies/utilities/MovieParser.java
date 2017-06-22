package com.popularmovies.popularmovies.utilities;

import android.database.Cursor;
import android.util.Log;

import com.popularmovies.popularmovies.data.FavoriteMovieContract;
import com.popularmovies.popularmovies.models.Movie;
import com.popularmovies.popularmovies.models.Review;

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
    private static final String KEY_MOVIE_ID = "id";

    /*keys for review*/
    private static final String KEY_REVIEW_AUTHOR = "author";
    private static final String KEY_REVIEW_CONTENT = "content";

    public static  Movie parserMovie(JSONObject movieJSONObject){
        Movie  movie = null;
        try {
            String id = movieJSONObject.getString(KEY_MOVIE_ID);
            String title = movieJSONObject.getString(KEY_TITLE);
            String thumbnailURL = movieJSONObject.getString(KEY_THUMBNAIL_URL);
            String synopsis = movieJSONObject.getString(KEY_SYNOPSIS);
            String releaseDate = movieJSONObject.getString(KEY_RELEASE_DATE);
            double userRating = movieJSONObject.getDouble(KEY_USER_RATING);

            movie = new Movie(id, title, thumbnailURL, synopsis, releaseDate, userRating);
        }catch (JSONException e){
            Log.e(TAG, e.getMessage());
        }

        return movie;
    }

    public static Movie parserMovie(Cursor currentCursor) {
        Movie movie;
        String id = currentCursor.getString(currentCursor.getColumnIndex(
                FavoriteMovieContract.FavoriteMovieEntry.COLUMN_MOVIE_ID
        ));
        String title = currentCursor.getString(currentCursor.getColumnIndex(
                FavoriteMovieContract.FavoriteMovieEntry.COLUMN_MOVIE_TITLE
        ));
        String thumbnailURL = currentCursor.getString(currentCursor.getColumnIndex(
                FavoriteMovieContract.FavoriteMovieEntry.COLUMN_MOVIE_THUMBNAIL_URL
        ));

        String synopsis = currentCursor.getString(currentCursor.getColumnIndex(
                FavoriteMovieContract.FavoriteMovieEntry.COLUMN_SYNOPSIS
        ));

        String releaseDate = currentCursor.getString(currentCursor.getColumnIndex(
                FavoriteMovieContract.FavoriteMovieEntry.COLUMN_RELEASE_DATE
        ));

        double userRating = currentCursor.getDouble(currentCursor.getColumnIndex(
                FavoriteMovieContract.FavoriteMovieEntry.COLUMN_USER_RATING
        ));

        movie = new Movie(id, title, thumbnailURL, synopsis, releaseDate, userRating);

        return movie;

    }

    public Review parseMovieReview(JSONObject review) {

        String author = null, content = null;
        try {

            author = review.getString(KEY_REVIEW_AUTHOR);
            content = review.getString(KEY_REVIEW_AUTHOR);


        } catch (JSONException je) {

        }

        return new Review(author, content);
    }
}
