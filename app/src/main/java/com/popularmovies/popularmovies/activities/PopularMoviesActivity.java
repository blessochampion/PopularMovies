package com.popularmovies.popularmovies.activities;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
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
import com.popularmovies.popularmovies.adapters.FavoriteMoviePostersAdapter;
import com.popularmovies.popularmovies.adapters.MoviePostersAdapter;
import com.popularmovies.popularmovies.data.FavoriteMovieContract;
import com.popularmovies.popularmovies.models.Movie;
import com.popularmovies.popularmovies.utilities.MovieParser;
import com.popularmovies.popularmovies.utilities.NetworkUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class PopularMoviesActivity extends AppCompatActivity implements Response.ErrorListener,
        Response.Listener<JSONObject>, AdapterView.OnItemClickListener,
        LoaderManager.LoaderCallbacks<Cursor> {

    GridView mGridView;
    TextView mErrorMessageTextView;
    ProgressBar mLoadingIndicator;

    ArrayList<Movie> movies;
    MoviePostersAdapter adapter;
    FavoriteMoviePostersAdapter mFavoriteMoviePostersAdapter;
    Cursor mFavoriteMoviesCursor;

    public static final String KEY_MOVIES = "movies";
    public static final String KEY_SELECTED_MOVIE = "mMovie";
    private static final String KEY_ACTION_BAR_TITLE = "title";
    private static final String KEY_FAVORITE_MOVIE_CURSOR = "cursor";
    public static final int LOADER_ID = 100;

    private static final String TAG = PopularMoviesActivity.class.getSimpleName();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popular_movies);

        mGridView = (GridView) findViewById(R.id.gv_movie_posters);
        mGridView.setOnItemClickListener(this);
        mErrorMessageTextView = (TextView) findViewById(R.id.tv_error_message);
        mLoadingIndicator = (ProgressBar) findViewById(R.id.pb_loading_indicator);
        Context context = this;

        if (activityIsStartingForTheFirstTime(savedInstanceState)) {

            boolean userIsConnectedToTheInternet = NetworkUtils.isNetworkAvailable(context);
            if (!userIsConnectedToTheInternet) {
                String errorMessage = getString(R.string.no_network_error_message);
                showErrorMessage(errorMessage);
            } else {
                sortMoviesByMostPopular();
            }

        } else {
            if(actionBarTitleWasSaved(savedInstanceState)){
                String actionBarTitle = savedInstanceState.getString(KEY_ACTION_BAR_TITLE);
                setActionBarTitle(actionBarTitle);
            }

            if(favoriteMovieCursorWasSaved(savedInstanceState)){
                getSupportLoaderManager().initLoader(LOADER_ID, null, this);
                adapter = null;

            }else {
                movies = savedInstanceState.getParcelableArrayList(KEY_MOVIES);
                mFavoriteMoviesCursor = null;
                adapter = new MoviePostersAdapter(context, movies);
                mGridView.setAdapter(adapter);

            }
            showData();
        }

    }

    private boolean favoriteMovieCursorWasSaved(Bundle savedInstanceState) {
        return savedInstanceState.containsKey(KEY_FAVORITE_MOVIE_CURSOR);
    }

    private boolean actionBarTitleWasSaved(Bundle savedInstanceState) {
        return savedInstanceState.containsKey(KEY_ACTION_BAR_TITLE);
    }

    private boolean activityIsStartingForTheFirstTime(Bundle savedInstanceState) {
        return savedInstanceState == null || !savedInstanceState.containsKey(KEY_MOVIES) || !favoriteMovieCursorWasSaved(savedInstanceState);
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
            sortMoviesByMostPopular();

            return true;

        } else if (selectedSortOrderId == R.id.action_sort_by_top_rated) {
            sortMoviesByTopRated();

            return true;
        } else if (selectedSortOrderId == R.id.action_sort_by_favorite) {
            sortMoviesByFavorite();

        }

        return super.onOptionsItemSelected(item);
    }

    private void sortMoviesByMostPopular() {
        String url = NetworkUtils.getPopularMovieURL();
        makeNetworkCallForMovies(url);

        String popularMovies = getString(R.string.popular_movies);
        setActionBarTitle(popularMovies);
    }

    private void sortMoviesByTopRated() {
        String url = NetworkUtils.getTopRatedMovieURL();
        makeNetworkCallForMovies(url);

        String topRatedMovies = getString(R.string.top_rated_movies);
        setActionBarTitle(topRatedMovies);
    }

    private void sortMoviesByFavorite() {
        String favoriteMovies = getString(R.string.favorite_movies);
        setActionBarTitle(favoriteMovies);
        getSupportLoaderManager().initLoader(LOADER_ID, null, this);
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
        super.onSaveInstanceState(outState);
        boolean moviesHasBeenDownloadedFromServer = adapter != null;
        if (moviesHasBeenDownloadedFromServer) {
            outState.putParcelableArrayList(KEY_MOVIES, movies);
        }
        else{
            outState.putInt(KEY_FAVORITE_MOVIE_CURSOR, LOADER_ID);
        }
        String actionBarTitle = getSupportActionBar().getTitle().toString();
        outState.putString(KEY_ACTION_BAR_TITLE, actionBarTitle);

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
        Movie selectedMovie;
        boolean currentAdapterIsForFavoriteMovies = (adapter == null);

        if (currentAdapterIsForFavoriteMovies) {
            Cursor currentCursor = (Cursor) mFavoriteMoviePostersAdapter.getItem(position);
            selectedMovie = MovieParser.parserMovie(currentCursor);


        } else {
            selectedMovie = adapter.getItem(position);
            getSupportLoaderManager().destroyLoader(LOADER_ID);
        }

        Context context = this;
        Class classToBeStartedViaIntent = MovieDetailsActivity.class;
        Intent movieDetailsIntent = new Intent(context, classToBeStartedViaIntent);
        movieDetailsIntent.putExtra(KEY_SELECTED_MOVIE, selectedMovie);

        startActivity(movieDetailsIntent);
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(this) {

            Cursor favoriteMovieCursor;

            @Override
            protected void onStartLoading() {
                super.onStartLoading();
                mLoadingIndicator.setVisibility(View.VISIBLE);

                if (favoriteMovieCursor == null)
                    forceLoad();
                else
                    deliverResult(favoriteMovieCursor);
            }

            @Override
            public Cursor loadInBackground() {

                Cursor favoriteMoviesCursor =
                        getContext().getContentResolver().query(
                                FavoriteMovieContract.FavoriteMovieEntry.CONTENT_URI, null,
                                null, null, null
                        );

                return favoriteMoviesCursor;
            }

            @Override
            protected void onStopLoading() {
                super.onStopLoading();
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
            mLoadingIndicator.setVisibility(View.INVISIBLE);
            if (data != null) {
                int count = data.getCount();
                if (count == 0) {
                    String noFavoriteMoviesInfoMessage = getString(R.string.info_no_favorite_movie);
                    showErrorMessage(noFavoriteMoviesInfoMessage);
                } else {
                    mFavoriteMoviesCursor = data;
                    Context context = PopularMoviesActivity.this;

                    mFavoriteMoviePostersAdapter = new FavoriteMoviePostersAdapter(context, mFavoriteMoviesCursor);
                    adapter = null;

                    mGridView.setAdapter(mFavoriteMoviePostersAdapter);
                    mFavoriteMoviePostersAdapter.notifyDataSetChanged();

                }

            } else {
                String databaseErrorMessage = getString(R.string.error_unable_to_fetch_favorite_movies);
                showErrorMessage(databaseErrorMessage);

        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
