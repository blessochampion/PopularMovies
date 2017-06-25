package com.popularmovies.popularmovies.utilities;

import android.util.Log;

import com.popularmovies.popularmovies.models.Trailer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by blessochampion on 6/25/17.
 */

public class TrailerParser {

    private static final String TAG = TrailerParser.class.getSimpleName();
    private static final String KEY_TRAILER_KEY = "key";
    private static final String KEY_TYPE = "type";
    private static final String KEY_NAME = "name";
    private static final String KEY_SITE = "site";
    private static final String KEY_RESULTS = "results";
    private static final String TRAILER_TYPE = "Trailer";
    private static final String TRAILER_SITE = "YouTube";

    public static List<Trailer> parseTrailer(JSONObject responseFromServer) {

        List<Trailer> trailers = new ArrayList<>();
        JSONObject currentTrailer;
        String key, name;

        try {
            JSONArray trailerArr = responseFromServer.getJSONArray(KEY_RESULTS);


            for (int i = 0; i < trailerArr.length(); i++) {
                currentTrailer = trailerArr.getJSONObject(i);

                if (currentTrailer.getString(KEY_TYPE).equals(TRAILER_TYPE) &&
                        currentTrailer.getString(KEY_SITE).equals(TRAILER_SITE)) {
                    key = currentTrailer.getString(KEY_TRAILER_KEY);
                    name = currentTrailer.getString(KEY_NAME);
                    trailers.add(new Trailer(key, name));
                }
            }
        } catch (JSONException js) {
            Log.e(TAG, js.getMessage());
        }


        return trailers;
    }
}
