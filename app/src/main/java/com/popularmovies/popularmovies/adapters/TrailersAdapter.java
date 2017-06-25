package com.popularmovies.popularmovies.adapters;

import android.content.Context;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.popularmovies.popularmovies.R;
import com.popularmovies.popularmovies.models.Trailer;

import java.util.List;

/**
 * Created by blessochampion on 6/25/17.
 */

public class TrailersAdapter extends ArrayAdapter<Trailer>
{


    public TrailersAdapter(@NonNull Context context, List<Trailer> trailers) {
        super(context, 0, trailers);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.trailer, parent, false);
        }
        TextView name = (TextView) convertView.findViewById(R.id.tv_name);
        Trailer currentTrailer = getItem(position);
        name.setText(currentTrailer.getName());
        return convertView;
    }
}
