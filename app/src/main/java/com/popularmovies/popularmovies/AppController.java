package com.popularmovies.popularmovies;

import android.app.Application;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by blessochampion on 4/17/17.
 */

public class AppController extends Application
{
    private static final String TAG = AppController.class.getSimpleName();

    private RequestQueue mRequestQueue;

    private static AppController sInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
    }

    public synchronized static AppController getInstance(){
        return  sInstance;
    }

    public RequestQueue getRequestQueue(){
        if(mRequestQueue == null){
            mRequestQueue = new Volley().newRequestQueue(getApplicationContext());
        }

        return  mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req, String tag){
        //set the default tag if the tag is empty
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req){
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    public void cancelPendingRequest(Object tag){
        if(mRequestQueue != null){
            mRequestQueue.cancelAll(tag);
        }
    }
}
