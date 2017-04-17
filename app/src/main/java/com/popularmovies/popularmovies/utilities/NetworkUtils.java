package com.popularmovies.popularmovies.utilities;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;

import com.popularmovies.popularmovies.Config;

/**
 * Created by blessochampion on 4/16/17.
 */

public class NetworkUtils {
    private static final String TAG = NetworkUtils.class.getSimpleName();

    private static final String MOVIE_DB_BASE_URL = "https://api.themoviedb.org/3/movie/";
    private static final String POPULAR_MOVIE_URL = MOVIE_DB_BASE_URL + "popular";
    private static final String TOP_RATED_MOVIE_URL = MOVIE_DB_BASE_URL + "top_rated";
    static final String API_KEY_PARAM = "api_key";
    static final String POSTER_IMAGE_BASE_URL = "http://image.tmdb.org/t/p/";


    public static String getPopularMovieUrl() {
        String apiKey = Config.MOVIE_DB_API_KEY;
        Uri popularMovieUri = Uri.parse(POPULAR_MOVIE_URL).buildUpon().
                appendQueryParameter(API_KEY_PARAM, apiKey)
                .build();

        return popularMovieUri.toString();
    }

    public static String getTopRatedMovieUrl(){
        String apiKey = Config.MOVIE_DB_API_KEY;
        Uri topRatedMovieUri = Uri.parse(TOP_RATED_MOVIE_URL).buildUpon()
                .appendQueryParameter(API_KEY_PARAM, apiKey).build();

        return topRatedMovieUri.toString();
    }

    public static String getPosterImageBaseUrl(){
        return POSTER_IMAGE_BASE_URL;
    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }


}
