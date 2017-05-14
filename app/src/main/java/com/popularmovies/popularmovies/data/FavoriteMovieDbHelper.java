package com.popularmovies.popularmovies.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by blessochampion on 5/6/17.
 */

public class FavoriteMovieDbHelper extends SQLiteOpenHelper {

    static final String DATABASE_NAME = "favoritemovies.db";
    static final int VERSION = 1;

    public FavoriteMovieDbHelper(Context context ){
        super(context, DATABASE_NAME, null, VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String SQL_CREATE_TABLE = "CREATE TABLE " +
                FavoriteMovieContract.FavoriteMovieEntry.TABLE_NAME + " (" +
                FavoriteMovieContract.FavoriteMovieEntry._ID+ " INTEGER PRIMARY KEY AUTOINCREMENT, "+
                FavoriteMovieContract.FavoriteMovieEntry.COLUMN_MOVIE_ID + " INTEGER, "+
                FavoriteMovieContract.FavoriteMovieEntry.COLUMN_MOVIE_TITLE + " TEXT NOT NULL, "+
                FavoriteMovieContract.FavoriteMovieEntry.COLUMN_MOVIE_THUMBNAIL_URL + " TEXT NOT NULL, "+
                FavoriteMovieContract.FavoriteMovieEntry.COLUMN_SYNOPSIS + " TEXT NOT NULL, "+
                FavoriteMovieContract.FavoriteMovieEntry.COLUMN_RELEASE_DATE + " TEXT NOT NULL, "+
                FavoriteMovieContract.FavoriteMovieEntry.COLUMN_USER_RATING + " REAL NOT NULL);";

        db.execSQL(SQL_CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP IF EXISTS "+ FavoriteMovieContract.FavoriteMovieEntry.TABLE_NAME);
        onCreate(db);
    }
}
