package com.popularmovies.popularmovies.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.popularmovies.popularmovies.R;
import com.popularmovies.popularmovies.models.Movie;
import com.popularmovies.popularmovies.utilities.NetworkUtils;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by blessochampion on 4/17/17.
 */

public class MoviePostersAdapter extends ArrayAdapter<Movie> {
    Context context;
    public MoviePostersAdapter(Context context,  List<Movie> movies) {
        super(context, 0, movies);
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Movie currentMovie = getItem(position);

        if(convertView == null){
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.movie_posters_item, parent, false );
        }

        ImageView posterImageView = (ImageView) convertView.findViewById(R.id.iv_poster);

        String imageBaseURL = NetworkUtils.getPosterImageBaseURL();
        String imageWidthDescription = "w185";
        String fullThumbnailURL = imageBaseURL + imageWidthDescription + currentMovie.getThumbnailURL();
        Picasso.with(context).load(fullThumbnailURL).into(posterImageView);

        return convertView;
    }
}
