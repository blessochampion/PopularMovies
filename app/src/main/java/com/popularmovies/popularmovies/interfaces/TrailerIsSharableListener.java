package com.popularmovies.popularmovies.interfaces;

/**
 * Created by blessochampion on 6/25/17.
 */

public interface TrailerIsSharableListener {

    void onFirstTrailerAvailable(String trailerKey);
    void noTrailerAvailable();
}
