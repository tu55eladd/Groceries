package com.example.sigurd.groceries.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


/**
 * Created by Sigurd on 18.12.2015.
 */
public class GroceryDBHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 7;
    public static final String DATABASE_NAME = "groceries.db";
    public static final String DROP_GROCERY_TABLE = "DROP TABLE IF EXISTS "+GroceryDBContract.GroceryEntry.TABLE_NAME+" ;";
    public static final String DROP_DINNER_TABLE = "DROP TABLE IF EXISTS "+GroceryDBContract.DinnerEntry.TABLE_NAME+" ;";
    public static final String DROP_INGREDIENTS_TABLE = "DROP TABLE IF EXISTS "+GroceryDBContract.IngredientEntry.TABLE_NAME+" ;";

    public GroceryDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(GroceryDBContract.GROCERY_CREATE_TABLE);
        db.execSQL(GroceryDBContract.DINNER_CREATE_TABLE);
        db.execSQL(GroceryDBContract.DINNER_INGREDIENTS_CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Discard old db, only for testing purposes
        db.execSQL(DROP_GROCERY_TABLE);
        db.execSQL(DROP_DINNER_TABLE);
        db.execSQL(DROP_INGREDIENTS_TABLE);
        onCreate(db);
    }
}
