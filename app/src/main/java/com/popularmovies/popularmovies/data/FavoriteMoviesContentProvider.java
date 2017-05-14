package com.popularmovies.popularmovies.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by blessochampion on 5/14/17.
 */

public class FavoriteMoviesContentProvider extends ContentProvider
{
    private FavoriteMovieDbHelper mFavoriteMovieDbHelper;

    public static final int FAVORITE_MOVIES = 100;
    public static final int FAVORITE_MOVIES_WITH_ID = 101;

    private static UriMatcher sUriMatcher = buildUriMatcher();

    public static  UriMatcher buildUriMatcher(){
        UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        matcher.addURI(FavoriteMovieContract.AUTHORITY, FavoriteMovieContract.FAVORITE_MOVIES_PATH, FAVORITE_MOVIES);
        matcher.addURI(FavoriteMovieContract.AUTHORITY, FavoriteMovieContract.FAVORITE_MOVIES_PATH+"/#", FAVORITE_MOVIES_WITH_ID);

        return  matcher;
    }

    @Override
    public boolean onCreate() {

        Context context = getContext();
        mFavoriteMovieDbHelper = new FavoriteMovieDbHelper(context);

        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {

        final SQLiteDatabase db = mFavoriteMovieDbHelper.getReadableDatabase();

        int match = sUriMatcher.match(uri);

        Cursor returnCursor = null;

        switch (match){

            case FAVORITE_MOVIES:

                returnCursor =   db.query(
                        FavoriteMovieContract.FavoriteMovieEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        null

                );
                break;
            case FAVORITE_MOVIES_WITH_ID:
                break;
            default:
                new UnsupportedOperationException("Unknown uri: "+ uri);
        }

        returnCursor.setNotificationUri(getContext().getContentResolver(), uri);


        return returnCursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {

        final SQLiteDatabase db = mFavoriteMovieDbHelper.getWritableDatabase();

        int match = sUriMatcher.match(uri);

        Uri  returnUri;
        switch (match){

            case FAVORITE_MOVIES:
                long id = db.insert(FavoriteMovieContract.FavoriteMovieEntry.TABLE_NAME, null, values);

                if(id > 0){
                    returnUri = ContentUris.withAppendedId(FavoriteMovieContract.FavoriteMovieEntry
                    .CONTENT_URI, id);
                }else {
                    throw  new SQLiteException("Failed to insert into row");
                }

                break;

            default:
                throw new UnsupportedOperationException("Unknown uri: "+ uri);

        }

        getContext().getContentResolver().notifyChange(uri, null);

        return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }
}
