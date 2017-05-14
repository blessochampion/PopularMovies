package com.popularmovies.popularmovies.adapters;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.popularmovies.popularmovies.R;
import com.popularmovies.popularmovies.data.FavoriteMovieContract;
import com.popularmovies.popularmovies.models.Movie;
import com.popularmovies.popularmovies.utilities.NetworkUtils;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by blessochampion on 4/17/17.
 */

public class FavoriteMoviePostersAdapter extends CursorAdapter {
    Context context;

    public FavoriteMoviePostersAdapter(Context context, Cursor cursor) {
        super(context, cursor, true);
        this.context = context;

    }


    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);

        return inflater.inflate(R.layout.movie_posters_item, parent, false );
    }

    @Override
    public void bindView(View convertView, Context context, Cursor cursor) {
        String thumbnailURL =  getThumbnailURLFrom(cursor);
        String title = getMoveTitleFrom(cursor);
        String movieId = getMovieId(cursor);
        convertView.setTag(movieId);


        ImageView posterImageView = (ImageView) convertView.findViewById(R.id.iv_poster);
        posterImageView.setContentDescription(title);

        String imageBaseURL = NetworkUtils.getPosterImageBaseURL();
        String imageWidthDescription = "w185";
        String fullThumbnailURL = imageBaseURL + imageWidthDescription + thumbnailURL;
        Picasso.with(context).load(fullThumbnailURL).into(posterImageView);
    }

    private String getMoveTitleFrom(Cursor cursor) {
        return  cursor.getString(cursor.getColumnIndex(
                FavoriteMovieContract.FavoriteMovieEntry.COLUMN_MOVIE_TITLE
        ));
    }

    private String getThumbnailURLFrom(Cursor cursor) {
        return  cursor.getString(cursor.getColumnIndex(
                FavoriteMovieContract.FavoriteMovieEntry.COLUMN_MOVIE_THUMBNAIL_URL
        ));
    }

    public String getMovieId(Cursor cursor) {
        return  cursor.getString(cursor.getColumnIndex(
                FavoriteMovieContract.FavoriteMovieEntry.COLUMN_MOVIE_ID
        ));
    }
}
