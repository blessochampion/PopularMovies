package com.popularmovies.popularmovies.activities;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.popularmovies.popularmovies.R;
import com.popularmovies.popularmovies.data.FavoriteMovieContract;
import com.popularmovies.popularmovies.models.Movie;
import com.popularmovies.popularmovies.utilities.NetworkUtils;
import com.squareup.picasso.Picasso;

public class MovieDetailsActivity extends AppCompatActivity {

    Movie movie;

    TextView mTitle;
    ImageView mPosterImageView;
    TextView mYear;
    TextView mDuration;
    TextView mRatings;
    TextView mSynopsis;
    Button mMarkAsFavorite;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        mTitle = (TextView) findViewById(R.id.tv_title);
        mPosterImageView = (ImageView) findViewById(R.id.iv_poster);
        mYear = (TextView) findViewById(R.id.tv_year);
        mDuration = (TextView) findViewById(R.id.tv_duration);
        mRatings = (TextView) findViewById(R.id.tv_ratings);
        mSynopsis = (TextView) findViewById(R.id.synopsis);
        mMarkAsFavorite = (Button) findViewById(R.id.btn_mark_as_favorite);
        mMarkAsFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertMovieIntoDatabase();
            }
        });

        enableActionBar();
        setActionBarTitle();

        Intent intentThatStartedThisActivity = getIntent();
        if(intentThatStartedThisActivity != null){
            if(intentThatStartedThisActivity.hasExtra(PopularMoviesActivity.KEY_SELECTED_MOVIE)){
                movie = intentThatStartedThisActivity.getParcelableExtra(PopularMoviesActivity.KEY_SELECTED_MOVIE);
                bindData();
            }
        }


    }

    private void setActionBarTitle() {
        String movieDetailsTitle = getString(R.string.movie_details_title);
        getSupportActionBar().setTitle(movieDetailsTitle);
    }

    private void enableActionBar() {
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }


    private void bindData() {
        String title = movie.getTitle();
        mTitle.setText(title);

        String year = extractYearFromReleaseDate(movie.getReleaseDate());
        mYear.setText(year);


        String ratings = movie.getUserRating() + "/10";
        mRatings.setText(ratings);

        String synopsis = movie.getSynopsis();
        mSynopsis.setText(synopsis);

        Context context = this;
        String imageBaseURL = NetworkUtils.getPosterImageBaseURL();
        String imageWidthDescription = "w185";
        String fullThumbnailURL = imageBaseURL + imageWidthDescription + movie.getThumbnailURL();
        Picasso.with(context).load(fullThumbnailURL).into(mPosterImageView);

    }

    private String extractYearFromReleaseDate(String releaseDate) {
        return releaseDate.substring(0, 4);
    }

    private void insertMovieIntoDatabase(){

        ContentValues contentValues = parseMovieIntoContentValues();

        Uri uri = getContentResolver().insert(FavoriteMovieContract.FavoriteMovieEntry.CONTENT_URI, contentValues);

        if(uri != null){
            String addedFavoriteMovieToastMessage = getString(R.string.add_favorit_movie_toast_message);
            Toast.makeText(getBaseContext(),
                    addedFavoriteMovieToastMessage
                    , Toast.LENGTH_LONG).show();

        }else {
            finish();
        }
    }

    private ContentValues parseMovieIntoContentValues() {

        ContentValues contentValues = new ContentValues();
        contentValues.put(FavoriteMovieContract.FavoriteMovieEntry.COLUMN_MOVIE_ID, movie.getId());
        contentValues.put(FavoriteMovieContract.FavoriteMovieEntry.COLUMN_MOVIE_TITLE, movie.getTitle());
        contentValues.put(FavoriteMovieContract.FavoriteMovieEntry.COLUMN_MOVIE_THUMBNAIL_URL, movie.getThumbnailURL());
        contentValues.put(FavoriteMovieContract.FavoriteMovieEntry.COLUMN_SYNOPSIS, movie.getSynopsis());
        contentValues.put(FavoriteMovieContract.FavoriteMovieEntry.COLUMN_RELEASE_DATE, movie.getReleaseDate());
        contentValues.put(FavoriteMovieContract.FavoriteMovieEntry.COLUMN_USER_RATING, movie.getUserRating());

        return contentValues;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int selectedItemId = item.getItemId();
        if(selectedItemId == android.R.id.home) {
            onBackPressed();
            return  true;
        }

        return super.onOptionsItemSelected(item);
    }
}
