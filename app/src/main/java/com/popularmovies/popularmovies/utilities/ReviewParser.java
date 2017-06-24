package com.popularmovies.popularmovies.utilities;

import android.util.Log;

import com.popularmovies.popularmovies.models.Review;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Blessing.Ekundayo on 6/13/2017.
 */

public class ReviewParser
{
    private static final String TAG = ReviewParser.class.getSimpleName();
    private static final String KEY_AUTHOR = "author";
    private static final String KEY_CONTENT = "content";
    private static final String KEY_RESULTS = "results";
    public static List<Review> parseReview(JSONObject responseFromServer){

        String author, content;
        JSONObject currentReview;
        List<Review> reviews =  new ArrayList<>();
        try {
            JSONArray reviewsArr = responseFromServer.getJSONArray(KEY_RESULTS);


            for (int i = 0; i < reviewsArr.length(); i++) {
                currentReview = reviewsArr.getJSONObject(i);
                author = currentReview.getString(KEY_AUTHOR);
                content = currentReview.getString(KEY_CONTENT);
                reviews.add(new Review(author, content));
            }
        }catch (JSONException js){
            Log.e(TAG, js.getMessage());
        }

        return reviews;

    }
}
