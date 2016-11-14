package com.ferdyrodriguez.contactoscontentprovider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by ferdyrod on 11/7/16.
 */

public class MyDBHelper extends SQLiteOpenHelper {

    public static final int DB_VERSION = 1;
    public static final String DB_NAME = "ContactosDB.db";
    public static final String TABLE_NAME = "contactos";

    public static final String TABLE_CONTACTS = "CREATE TABLE " + TABLE_NAME +
            "( _id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, " +
            ContactContract.Contacto.COL_NAME + " varchar(60), " +
            ContactContract.Contacto.COL_CELLPHONE+ " varchar(12), " +
            ContactContract.Contacto.COL_PHONE + " varchar(12), " +
            ContactContract.Contacto.COL_EMAIL+ " varchar(50))";


    public MyDBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(TABLE_CONTACTS);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACTS);
        onCreate(sqLiteDatabase);
    }
}
