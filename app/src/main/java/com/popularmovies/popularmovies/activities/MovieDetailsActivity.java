package com.popularmovies.popularmovies.activities;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ShareActionProvider;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.popularmovies.popularmovies.R;
import com.popularmovies.popularmovies.adapters.ViewPagerAdapter;
import com.popularmovies.popularmovies.data.FavoriteMovieContract;
import com.popularmovies.popularmovies.fragments.ReviewsFragment;
import com.popularmovies.popularmovies.fragments.TrailerFragment;
import com.popularmovies.popularmovies.interfaces.TrailerIsSharableListener;
import com.popularmovies.popularmovies.models.Movie;
import com.popularmovies.popularmovies.utilities.NetworkUtils;
import com.squareup.picasso.Picasso;


public class MovieDetailsActivity extends AppCompatActivity implements TrailerIsSharableListener {

    Movie mMovie;

    TextView mTitle;
    ImageView mPosterImageView;
    TextView mYear;
    TextView mDuration;
    TextView mRatings;
    TextView mSynopsis;
    Button mMarkAsFavorite;
    TabLayout mTabLayout;
    ViewPager mViewPager;
    MenuItem mShareItem;

    ShareActionProvider mShareActionProvider;


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
        mTabLayout = (TabLayout) findViewById(R.id.tl_trailers_reviews);
        mViewPager = (ViewPager) findViewById(R.id.vp_trailers_reviews);
        enableActionBar();
        setActionBarTitle();

        Intent intentThatStartedThisActivity = getIntent();
        if(intentThatStartedThisActivity != null){
            if(intentThatStartedThisActivity.hasExtra(PopularMoviesActivity.KEY_SELECTED_MOVIE)){
                mMovie = intentThatStartedThisActivity.getParcelableExtra(PopularMoviesActivity.KEY_SELECTED_MOVIE);
                bindData();
            }
        }else{
            finish();
        }

        setupViewPager();
        mTabLayout.setupWithViewPager(mViewPager);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.movies_details, menu);

        mShareItem = menu.findItem(R.id.menu_trailer_share);
        mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(mShareItem);
        mShareItem.setVisible(false);
        MenuItemCompat.setActionProvider(mShareItem, mShareActionProvider);
        return true;
    }




    private void setupViewPager() {
        FragmentManager supportFragmentManager = getSupportFragmentManager();
        ViewPagerAdapter adapter = new ViewPagerAdapter(supportFragmentManager);
        adapter.addFragment(TrailerFragment.getInstance(mMovie.getId()), "Trailers");
        adapter.addFragment(ReviewsFragment.getInstance(mMovie.getId()), "Reviews");
        mViewPager.setAdapter(adapter);
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
        String title = mMovie.getTitle();
        mTitle.setText(title);

        String year = extractYearFromReleaseDate(mMovie.getReleaseDate());
        mYear.setText(year);


        String ratings = mMovie.getUserRating() + "/10";
        mRatings.setText(ratings);

        String synopsis = mMovie.getSynopsis();
        mSynopsis.setText(synopsis);

        Context context = this;
        String imageBaseURL = NetworkUtils.getPosterImageBaseURL();
        String imageWidthDescription = "w185";
        String fullThumbnailURL = imageBaseURL + imageWidthDescription + mMovie.getThumbnailURL();
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
        contentValues.put(FavoriteMovieContract.FavoriteMovieEntry.COLUMN_MOVIE_ID, mMovie.getId());
        contentValues.put(FavoriteMovieContract.FavoriteMovieEntry.COLUMN_MOVIE_TITLE, mMovie.getTitle());
        contentValues.put(FavoriteMovieContract.FavoriteMovieEntry.COLUMN_MOVIE_THUMBNAIL_URL, mMovie.getThumbnailURL());
        contentValues.put(FavoriteMovieContract.FavoriteMovieEntry.COLUMN_SYNOPSIS, mMovie.getSynopsis());
        contentValues.put(FavoriteMovieContract.FavoriteMovieEntry.COLUMN_RELEASE_DATE, mMovie.getReleaseDate());
        contentValues.put(FavoriteMovieContract.FavoriteMovieEntry.COLUMN_USER_RATING, mMovie.getUserRating());

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


    @Override
    public void onFirstTrailerAvailable(String trailerKey) {
        mShareItem.setVisible(true);
        String movieUrl = NetworkUtils.getMovieTrailerPreviewURL(trailerKey);
        setShareIntent(movieUrl);
    }

    @Override
    public void noTrailerAvailable() {
        mShareItem.setVisible(false);
    }

    private void setShareIntent(String trailerUrl) {
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_TEXT, trailerUrl);
        shareIntent.setType("text/plain");
        if (mShareActionProvider != null) {
            mShareActionProvider.setShareIntent(shareIntent);
        }
    }
}
