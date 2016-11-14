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
        SQLiteDatabase db = dbHelper.getWritableDatabase();

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
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        if (ContactContract.uriMatcher.match(uri) != ContactContract.ALL_CONTACTS){
            throw new IllegalArgumentException("Unsupported Uri " + uri);
        }
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        long rowId = db.insert(ContactContract.TABLE_NAME, null, contentValues);

        if(rowId > 0){
            Uri _uri = ContentUris.withAppendedId(ContactContract.CONTENT_URI, rowId);
            getContext().getContentResolver().notifyChange(_uri, null);
            return _uri;
        }
        throw new SQLException("Failed to insert to the Databsae " + uri);
    }



    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {

        int deletedRows = 0;
        int match = ContactContract.uriMatcher.match(uri);
        switch (match){
            case ContactContract.CONTACT_ID:
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                String _id = uri.getLastPathSegment();
                deletedRows = db.delete(ContactContract.TABLE_NAME, ContactContract.Contacto._ID + "=" + _id, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
        if(deletedRows > 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return  deletedRows;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String selection, String[] selectionArgs) {
        int updatedRows = 0;
        int match = ContactContract.uriMatcher.match(uri);
        switch (match){
            case ContactContract.CONTACT_ID:
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                String _id = uri.getLastPathSegment();
                updatedRows = db.update(ContactContract.TABLE_NAME, contentValues, ContactContract.Contacto._ID + "=" + _id, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
        if(updatedRows > 0){
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return  updatedRows;
    }
}
