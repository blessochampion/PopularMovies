package com.popularmovies.popularmovies.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.popularmovies.popularmovies.R;

/**
 * Created by Blessing.Ekundayo on 6/13/2017.
 */

public class ReviewsFragment extends Fragment {


        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_reviews, container, false);
            makeNetworkCall();
            return rootView;
        }

    private void makeNetworkCall() {

    }


}
