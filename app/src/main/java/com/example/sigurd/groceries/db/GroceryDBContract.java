package com.example.sigurd.groceries.db;

import android.provider.BaseColumns;
import android.support.v4.app.INotificationSideChannel;

/**
 * Created by Sigurd on 19.12.2015.
 */
public final class GroceryDBContract {

    public GroceryDBContract(){};

    /* Table entry names */

    public static abstract class GroceryEntry implements BaseColumns{
        public static final String TABLE_NAME = "groceries";
        public static final String COLUMN_NAME_GROCERY_ID = "groceryId";
        public static final String COLUMN_NAME_NAME = "name";
        public static final String COLUMN_NAME_PRICE = "price";
        public static final String COLUMN_NAME_ICON = "icon";
        public static final String COLUMN_NAME_ORDER = "groceryOrder";
        public static final String COLUMN_NAME_TYPE = "type";
    }

    public static abstract class DinnerEntry implements BaseColumns{
        public static final String TABLE_NAME = "dinners";
        public static final String COLUMN_NAME_DINNER_ID = "dinnerId";
        public static final String COLUMN_NAME_NAME = "name";
        public static final String COLUMN_NAME_ICON = "icon";
        public static final String COLUMN_NAME_ORDER = "dinnerOrder";
    }

    public static abstract class IngredientEntry implements BaseColumns{
        public static final String TABLE_NAME = "ingredients";
        public static final String COLUMN_NAME_GROCERY_ID = "groceryId";
        public static final String COLUMN_NAME_DINNER_ID = "dinnerId";
    }

    /* Create table statements */

    public static final String GROCERY_CREATE_TABLE = "CREATE TABLE "+GroceryEntry.TABLE_NAME+ "( "+
            GroceryEntry.COLUMN_NAME_GROCERY_ID +" INTEGER, " +
            GroceryEntry.COLUMN_NAME_NAME       +" TEXT, " +
            GroceryEntry.COLUMN_NAME_PRICE      +" REAL, " +
            GroceryEntry.COLUMN_NAME_ORDER      +" INTEGER, " +
            GroceryEntry.COLUMN_NAME_TYPE       +" INTEGER, " +
            GroceryEntry.COLUMN_NAME_ICON       +" TEXT, " +
            "PRIMARY KEY ("+GroceryEntry.COLUMN_NAME_GROCERY_ID+"));";

    public static final String DINNER_CREATE_TABLE = "CREATE TABLE "+DinnerEntry.TABLE_NAME+" ( " +
            DinnerEntry.COLUMN_NAME_DINNER_ID  +" INTEGER, " +
            DinnerEntry.COLUMN_NAME_NAME       +" TEXT, " +
            DinnerEntry.COLUMN_NAME_ICON       +" TEXT, "+
            DinnerEntry.COLUMN_NAME_ORDER      +" INTEGER, " +
            "PRIMARY KEY ("+DinnerEntry.COLUMN_NAME_DINNER_ID+"));";

    public static final String DINNER_INGREDIENTS_CREATE_TABLE = "CREATE TABLE "+ IngredientEntry.TABLE_NAME+" ( " +
            IngredientEntry.COLUMN_NAME_DINNER_ID +" INTEGER, " +
            IngredientEntry.COLUMN_NAME_GROCERY_ID +" INTEGER, " +
            "PRIMARY KEY ("+IngredientEntry.COLUMN_NAME_DINNER_ID+","+IngredientEntry.COLUMN_NAME_GROCERY_ID+"), " +
            "FOREIGN KEY ("+IngredientEntry.COLUMN_NAME_DINNER_ID+") REFERENCES "+DinnerEntry.TABLE_NAME+"("+DinnerEntry.COLUMN_NAME_DINNER_ID+")"+
            "FOREIGN KEY ("+IngredientEntry.COLUMN_NAME_GROCERY_ID+") REFERENCES "+GroceryEntry.TABLE_NAME+"("+GroceryEntry.COLUMN_NAME_GROCERY_ID+"));";

}
