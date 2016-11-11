package com.ferdyrodriguez.contactoscontentprovider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;


/**
 * Created by ferdyrod on 11/7/16.
 */

public class MyContentProvider extends ContentProvider {

    private SQLiteDatabase db;
    private MyDBHelper dbHelper;

    @Override
    public boolean onCreate() {
        dbHelper = new MyDBHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        String where = selection;
        if(ContactContract.uriMatcher.match(uri) == ContactContract.CONTACT_ID){
            where = "_id=" + uri.getLastPathSegment();
        }
        db = dbHelper.getWritableDatabase();

        Cursor cur = db.query(ContactContract.TABLE_NAME,
                projection,
                where,
                selectionArgs,
                null,
                null,
                sortOrder);
        return cur;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        int match = ContactContract.uriMatcher.match(uri);
        switch (match){
            case ContactContract.ALL_CONTACTS:
                return ContactContract.MULTIPLE_MIME;

            case ContactContract.CONTACT_ID:
                return ContactContract.SINGLE_MIME;

            default:
                return null;
        }
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        if(ContactContract.uriMatcher.match(uri) == ContactContract.CONTACT_ID){
            long rowId = db.insert(ContactContract.TABLE_NAME, null, contentValues);

            if(rowId > 0){
                Uri newUri = ContentUris.withAppendedId(ContactContract.CONTENT_URI, rowId);
                return newUri;
            } else {
                throw new SQLException("Failed to insert row into the Database");
            }

        } else {
            throw new IllegalArgumentException("URI unknown: " + uri);
        }
    }


    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {

        int deletedRows = 0;
        int match = ContactContract.uriMatcher.match(uri);
        switch (match){
            case ContactContract.CONTACT_ID:
                deletedRows = db.delete(ContactContract.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
        return  deletedRows;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String selection, String[] selectionArgs) {
        int updatedRows = 0;
        int match = ContactContract.uriMatcher.match(uri);
        switch (match){
            case ContactContract.CONTACT_ID:
                updatedRows = db.update(ContactContract.TABLE_NAME, contentValues, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
        return  updatedRows;
    }
}
