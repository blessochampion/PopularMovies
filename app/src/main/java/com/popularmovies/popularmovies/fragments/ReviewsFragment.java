package com.popularmovies.popularmovies.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.popularmovies.popularmovies.AppController;
import com.popularmovies.popularmovies.R;
import com.popularmovies.popularmovies.models.Review;
import com.popularmovies.popularmovies.utilities.NetworkUtils;
import com.popularmovies.popularmovies.utilities.ReviewParser;

import org.json.JSONObject;

import java.util.List;

/**
 * Created by Blessing.Ekundayo on 6/13/2017.
 */


public class ReviewsFragment extends Fragment implements Response.ErrorListener, Response.Listener<JSONObject> {
    private ProgressBar mReviewLoadingIndicator;
    private NestedScrollView mReviewContainer;
    private TextView mReviews;
    String mMovieId;
    public static final String KEY_MOVIE_ID = "MOVIE_ID";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMovieId = getArguments().getString(KEY_MOVIE_ID, "");
    }

    @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_reviews, container, false);
        mReviewLoadingIndicator = (ProgressBar) rootView.findViewById(R.id.pb_reviews_loading_indicator);
        mReviewContainer = (NestedScrollView) rootView.findViewById(R.id.sv_review_container);
        mReviews = (TextView) rootView.findViewById(R.id.tv_reviews);
            makeNetworkCall();
            return rootView;
        }

    private void makeNetworkCall() {
        String reviewUrl = NetworkUtils.getMovieReviewsURL(mMovieId);
        mReviewLoadingIndicator.setVisibility(View.VISIBLE);
        Log.e("rr", reviewUrl);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET
                , reviewUrl, null, this, this
        );

        AppController.getInstance().addToRequestQueue(jsonObjectRequest);
    }


    public static ReviewsFragment getInstance(String movieId) {
        ReviewsFragment reviewsFragment = new ReviewsFragment();
        Bundle bundle = new Bundle();
        bundle.putString(KEY_MOVIE_ID, movieId);
        reviewsFragment.setArguments(bundle);

        return reviewsFragment;
    }
    private void formatReviews(List<Review> reviews){

        String previousContent = "";
        for(Review review: reviews){
            previousContent += "\n\n<h4> <font color ='#FF4081'>"+ review.getAuthor() +": </font> " +
                    "</h4> " + review.getContent();

        }

        mReviews.setText(formatReviewToStyledVersion(previousContent));
    }



    private Spanned formatReviewToStyledVersion(String rawReview){
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            return  Html.fromHtml(rawReview,Html.FROM_HTML_MODE_LEGACY);
        } else {
            return  Html.fromHtml(rawReview);
        }
    }

    @Override
    public void onResponse(JSONObject response) {
        List<Review> reviews = ReviewParser.parseReview(response);
        mReviewLoadingIndicator.setVisibility(View.GONE);
        mReviewContainer.setVisibility(View.VISIBLE);
        formatReviews(reviews);


    }

    @Override
    public void onErrorResponse(VolleyError error) {
        mReviewLoadingIndicator.setVisibility(View.GONE);
        mReviewContainer.setVisibility(View.VISIBLE);
        mReviews.setText(error.toString());
    }

}
