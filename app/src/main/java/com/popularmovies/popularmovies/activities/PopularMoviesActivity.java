package com.popularmovies.popularmovies.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.popularmovies.popularmovies.AppController;
import com.popularmovies.popularmovies.R;
import com.popularmovies.popularmovies.adapters.MoviePostersAdapter;
import com.popularmovies.popularmovies.models.Movie;
import com.popularmovies.popularmovies.utilities.MovieParser;
import com.popularmovies.popularmovies.utilities.NetworkUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class PopularMoviesActivity extends AppCompatActivity implements Response.ErrorListener, Response.Listener<JSONObject>, AdapterView.OnItemClickListener {

    GridView mGridView;
    TextView mErrorMessageTextView;
    ProgressBar mLoadingIndicator;

    ArrayList<Movie> movies;
    MoviePostersAdapter adapter;

    public static final String KEY_MOVIES = "movies";
    public static final String KEY_SELECTED_MOVIE = "movie";
    private static final String KEY_ACTION_BAR_TITLE = "title";

    private static final String TAG = PopularMoviesActivity.class.getSimpleName();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popular_movies);

        mGridView = (GridView) findViewById(R.id.gv_movie_posters);

        mGridView.setOnItemClickListener(this);

        mErrorMessageTextView = (TextView) findViewById(R.id.tv_error_message);
        mLoadingIndicator = (ProgressBar) findViewById(R.id.pb_loading_indicator);

        if (savedInstanceState == null || !savedInstanceState.containsKey(KEY_MOVIES)) {
            Context context = this;
            boolean userIsConnectedToTheInternet = NetworkUtils.isNetworkAvailable(context);

            if (!userIsConnectedToTheInternet) {
                String errorMessage = getString(R.string.no_network_error_message);
                showErrorMessage(errorMessage);

            } else {
                String popularMovieUrl = NetworkUtils.getPopularMovieUrl();
                makeNetworkCallForMovies(popularMovieUrl);

                String popularMovies = getString(R.string.popular_movies);
                setActionBarTitle(popularMovies);
            }

        } else {
            movies = savedInstanceState.getParcelableArrayList(KEY_MOVIES);
            Context context = this;
            adapter = new MoviePostersAdapter(context, movies);
            mGridView.setAdapter(adapter);
            showData();

            if(savedInstanceState.containsKey(KEY_ACTION_BAR_TITLE)){
                String actionBarTitle = savedInstanceState.getString(KEY_ACTION_BAR_TITLE);
                setActionBarTitle(actionBarTitle);
            }
        }

    }

    private void setActionBarTitle(String title) {
        getSupportActionBar().setTitle(title);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.movies_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int selectedSortOrderId = item.getItemId();

        if (selectedSortOrderId == R.id.action_sort_by_most_popular) {
            String url = NetworkUtils.getPopularMovieUrl();
            makeNetworkCallForMovies(url);

            String popularMovies = getString(R.string.popular_movies);
            setActionBarTitle(popularMovies);

            return true;

        } else if (selectedSortOrderId == R.id.action_sort_by_top_rated) {
            String url = NetworkUtils.getTopRatedMovieUrl();
            makeNetworkCallForMovies(url);

            String topRatedMovies = getString(R.string.top_rated_movies);
            setActionBarTitle(topRatedMovies);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void makeNetworkCallForMovies(String moviesURL) {
        mLoadingIndicator.setVisibility(View.VISIBLE);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET
                , moviesURL, null, this, this
        );

        AppController.getInstance().addToRequestQueue(jsonObjectRequest);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        boolean moviesHasBeenDownloadedFromServer = movies != null;
        if (moviesHasBeenDownloadedFromServer)
            outState.putParcelableArrayList(KEY_MOVIES, movies);

        String actionBarTitle = getSupportActionBar().getTitle().toString();
        outState.putString(KEY_ACTION_BAR_TITLE, actionBarTitle);

        super.onSaveInstanceState(outState);
    }

    private void showErrorMessage(String errorMessage) {
        mGridView.setVisibility(View.INVISIBLE);
        mErrorMessageTextView.setVisibility(View.VISIBLE);
        mErrorMessageTextView.setText(errorMessage);
    }

    private void showData() {
        mErrorMessageTextView.setVisibility(View.INVISIBLE);
        mGridView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        mLoadingIndicator.setVisibility(View.GONE);
        String errorMessage = getString(R.string.error_unable_to_fetch_movies);
        showErrorMessage(errorMessage);

    }

    @Override
    public void onResponse(JSONObject response) {
        mLoadingIndicator.setVisibility(View.INVISIBLE);
        String KEY_RESULT = "results";

        try {

            JSONArray moviesJSONArray = response.getJSONArray(KEY_RESULT);

            movies = new ArrayList<>();
            for (int i = 0; i < moviesJSONArray.length(); i++) {
                JSONObject currentMovieJSONObject = moviesJSONArray.getJSONObject(i);
                Log.e(TAG, currentMovieJSONObject.toString());
                Movie currentMovie = MovieParser.parserMovie(currentMovieJSONObject);
                movies.add(currentMovie);
            }

            Context context = this;
            adapter = new MoviePostersAdapter(context, movies);
            mGridView.setAdapter(adapter);
            showData();

        } catch (JSONException jsone) {
            Log.e(TAG, jsone.getMessage());
        }


    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Movie selectedMovie = adapter.getItem(position);

        Context context = this;
        Class classToBeStartedViaIntent = MovieDetailsActivity.class;
        Intent movieDetailsIntent = new Intent(context, classToBeStartedViaIntent);
        movieDetailsIntent.putExtra(KEY_SELECTED_MOVIE, selectedMovie);

        startActivity(movieDetailsIntent);
    }
}
