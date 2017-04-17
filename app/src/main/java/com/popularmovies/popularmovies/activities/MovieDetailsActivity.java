package com.popularmovies.popularmovies.activities;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.popularmovies.popularmovies.R;
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

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        String movieDetailsTitle = getString(R.string.movie_details_title);
        getSupportActionBar().setTitle(movieDetailsTitle);

        Intent intentThatStartedThisActivity = getIntent();
        if(intentThatStartedThisActivity != null){
            if(intentThatStartedThisActivity.hasExtra(PopularMoviesActivity.KEY_SELECTED_MOVIE)){
                movie = intentThatStartedThisActivity.getParcelableExtra(PopularMoviesActivity.KEY_SELECTED_MOVIE);
                bindData();
            }
        }


    }

    private void bindData() {
        String title = movie.getTitle();
        mTitle.setText(title);

        String year = extractYearFromReleaseDate(movie.getReleaseDate());
        mYear.setText(year);

        /*Todo: Duration*/

        String ratings = movie.getUserRating() + "/10";
        mRatings.setText(ratings);

        String synopsis = movie.getSynopsis();
        mSynopsis.setText(synopsis);

        Context context = this;
        String imageBaseURL = NetworkUtils.getPosterImageBaseUrl();
        String imageWidthDescription = "w185";
        String fullThumbnailURL = imageBaseURL + imageWidthDescription + movie.getThumbnailURL();
        Picasso.with(context).load(fullThumbnailURL).into(mPosterImageView);

    }

    private String extractYearFromReleaseDate(String releaseDate) {
        return releaseDate.substring(releaseDate.length()-4);
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
