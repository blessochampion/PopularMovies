package com.popularmovies.popularmovies.models;

/**
 * Created by blessochampion on 6/25/17.
 */

public class Trailer {
    private String key;
    private String name;

    public Trailer(String key, String name) {

        this.key = key;
        this.name = name;
    }

    public String getKey() {
        return key;
    }

    public String getName() {
        return name;
    }
}
