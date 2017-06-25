package com.popularmovies.popularmovies.fragments;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.popularmovies.popularmovies.AppController;
import com.popularmovies.popularmovies.R;
import com.popularmovies.popularmovies.adapters.TrailersAdapter;
import com.popularmovies.popularmovies.interfaces.TrailerIsSharableListener;
import com.popularmovies.popularmovies.models.Trailer;
import com.popularmovies.popularmovies.utilities.NetworkUtils;
import com.popularmovies.popularmovies.utilities.TrailerParser;

import org.json.JSONObject;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class TrailerFragment extends Fragment implements Response.Listener<JSONObject>, Response.ErrorListener, AdapterView.OnItemClickListener {

    private static String TAG = TrailerFragment.class.getSimpleName();
    private static String KEY_MOVIE_ID = "id";
    private String mMovieId;
    private TextView mInfoTextView;
    private ProgressBar mTrailerLoadingIndicator;
    private ListView mTrailersListView;
    private TrailersAdapter mTrailerAdapter;

    private TrailerIsSharableListener mTrailerIsSharableListener;


    public TrailerFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mTrailerIsSharableListener = (TrailerIsSharableListener) context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMovieId = getArguments().getString(KEY_MOVIE_ID, "");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_trailer, container, false);
        mInfoTextView = (TextView) rootView.findViewById(R.id.tv_info_message);
        mTrailerLoadingIndicator = (ProgressBar) rootView.findViewById(R.id.pb_trailer_loading_indicator);
        mTrailersListView = (ListView) rootView.findViewById(R.id.lv_trailers);
        mTrailersListView.setOnItemClickListener(this);

        makeNetworkCall();

        return  rootView;
    }

    private void makeNetworkCall() {
        String trailerURLUrl = NetworkUtils.getMovieTrailerUrl(mMovieId);
        mTrailerLoadingIndicator.setVisibility(View.VISIBLE);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET
                , trailerURLUrl, null, this, this
        );

        AppController.getInstance().addToRequestQueue(jsonObjectRequest);
    }



    public static TrailerFragment getInstance(String movieId){
        TrailerFragment trailerFragment = new TrailerFragment();
        Bundle bundle = new Bundle();
        bundle.putString(KEY_MOVIE_ID, movieId);
        trailerFragment.setArguments(bundle);
        return  trailerFragment;
    }

    @Override
    public void onResponse(JSONObject response) {
        List<Trailer> trailers = TrailerParser.parseTrailer(response);
        mTrailerLoadingIndicator.setVisibility(View.GONE);
        if(trailers.isEmpty()){
            mInfoTextView.setText(getString(R.string.no_trailer_info));
            if(mTrailerIsSharableListener != null)
                mTrailerIsSharableListener.noTrailerAvailable();

        }else {
            mTrailerIsSharableListener.onFirstTrailerAvailable(trailers.get(0).getKey());
            Context context = getActivity();
            mTrailerAdapter = new TrailersAdapter(context, trailers);
            mTrailersListView.setAdapter(mTrailerAdapter);
            showData();
        }
    }

    private void showData(){
        mTrailersListView.setVisibility(View.VISIBLE);
        mInfoTextView.setVisibility(View.GONE);
    }

    @Override
    public void onErrorResponse(VolleyError error) {

        Log.e(TAG, error.getMessage());
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Trailer selectedTrailer = mTrailerAdapter.getItem(position);
        String trailerURl = NetworkUtils.getMovieTrailerPreviewURL(selectedTrailer.getKey());
        openTrailer(trailerURl);
    }

    public void openTrailer(String url) {
        Uri webpage = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
        if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivity(intent);
        }
    }
}
