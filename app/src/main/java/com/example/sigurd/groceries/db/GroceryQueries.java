package com.example.sigurd.groceries.db;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import com.example.sigurd.groceries.models.Dinner;
import com.example.sigurd.groceries.models.Grocery;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by Sigurd on 19.12.2015.
 */
public class GroceryQueries {

    private GroceryDBHelper helper;
    private SQLiteDatabase db;
    private ArrayList<Grocery> allDinners;
    private Context context;
    private final String TAG = this.getClass().getSimpleName();

    public GroceryQueries(Context context){
        helper = new GroceryDBHelper(context);
        this.context = context;

    }

    public ArrayList<Grocery> getGroceries(){
        // Returns all groceries in database
        db = helper.getReadableDatabase();
        String[] projection = {"*"};
        Cursor c = db.query(
                GroceryDBContract.GroceryEntry.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                GroceryDBContract.GroceryEntry.COLUMN_NAME_ORDER
        );

        if(c.moveToFirst()==false) return null;
        ArrayList<Grocery> allGroceries = new ArrayList<>();
        do{
            int ID      = c.getInt(0);
            String name = c.getString(1);
            double price= c.getDouble(2);
            int order   = c.getInt(3);
            int type    = c.getInt(4);
            String icon = c.getString(5);
            Uri iconUri = icon.equals("") ? null : Uri.parse(icon);
            allGroceries.add( new Grocery(ID,name,price,order,type,iconUri));
        }while(c.moveToNext());
        db.close();
        return allGroceries;
    }

    public long saveGrocery(Grocery grocery){
        db = helper.getWritableDatabase();
        ContentValues values = getContentValuesForGrocery(grocery);
        values.remove(GroceryDBContract.GroceryEntry.COLUMN_NAME_GROCERY_ID);
        // Only for debugging
        int type = values.getAsInteger(GroceryDBContract.GroceryEntry.COLUMN_NAME_TYPE);
        long groceryId = db.insert(GroceryDBContract.GroceryEntry.TABLE_NAME,null,values);
        db.close();
        return groceryId;
    }

    public long saveDinner(Dinner dinner){
        db = helper.getWritableDatabase();
        ContentValues values = getContentValuesForDinner(dinner);
        //TODO: save ingredients here ? (Should roll back if failed)
        long affectedRows = db.insert(GroceryDBContract.DinnerEntry.TABLE_NAME, null, values);
        db.close();
        return affectedRows;
    }

    public long saveIngredients(Dinner dinner){
        return 0;
    }

    public int updateGrocery(Grocery grocery){
        db = helper.getWritableDatabase();
        ContentValues values = getContentValuesForGrocery(grocery);
        String[] args = {grocery.getID()+""};
        int affRows = db.update(GroceryDBContract.GroceryEntry.TABLE_NAME, values, GroceryDBContract.GroceryEntry.COLUMN_NAME_GROCERY_ID + "=?", args);
        db.close();
        return affRows;
    }

    public int updateGroceryStatuses(Map<Integer,Grocery> groceries) {
        int count = 0;
        for(Integer key : groceries.keySet()){
            int i = updateGroceryStatus(groceries.get(key));
            count += i;
        }
        return count;
    }

    public int updateGroceryStatus(Grocery grocery){
        db = helper.getWritableDatabase();
        String tableName = GroceryDBContract.GroceryEntry.TABLE_NAME;
        ContentValues values = new ContentValues();
        values.put(GroceryDBContract.GroceryEntry.COLUMN_NAME_TYPE,grocery.getType());
        String where = GroceryDBContract.GroceryEntry.COLUMN_NAME_GROCERY_ID+"=?";
        String[] args = {grocery.getID()+""};
        return db.update(tableName,values,where,args);
    }

    public int updateDinner(Dinner newDinner, Dinner oldDinner){
        updateIngredients(newDinner,oldDinner);
        db = helper.getWritableDatabase();
        ContentValues values = getContentValuesForDinner(newDinner);
        String[] args = {newDinner.getID()+""};
        int affRows = db.update(GroceryDBContract.DinnerEntry.TABLE_NAME, values, GroceryDBContract.DinnerEntry.COLUMN_NAME_DINNER_ID + "=?", args);
        db.close();
        return affRows;
    }

    private int updateIngredients(Dinner newDinner, Dinner oldDinner){
        db = helper.getWritableDatabase();
        // Get all ingredients that differs
        ArrayList<Grocery> oldIngredients = oldDinner.getIngredients();
        ArrayList<Grocery> newIngredients = newDinner.getIngredients();
        HashSet<Grocery> removeIngredients = new HashSet<>();
        HashSet<Grocery> addIngredients = new HashSet<>();
        // All ingredients that are to be removed
        removeIngredients.addAll(oldIngredients);
        removeIngredients.removeAll(newIngredients);
        // All ingredients that are to be added
        addIngredients.addAll(newIngredients);
        addIngredients.removeAll(oldIngredients);

        String dID = newDinner.getID()+"";
        db.beginTransaction();
        try {
            for(Grocery ingredient : removeIngredients){
                String[] dinnerIdAndGroceryId = {dID,ingredient.getID()+""};
                db.delete(GroceryDBContract.IngredientEntry.TABLE_NAME,
                       GroceryDBContract.IngredientEntry.COLUMN_NAME_DINNER_ID+"=? AND"+
                       GroceryDBContract.IngredientEntry.COLUMN_NAME_GROCERY_ID+"=?",
                       dinnerIdAndGroceryId);
            }
            for(Grocery ingredient : addIngredients){
                ContentValues values = new ContentValues();
                values.put(GroceryDBContract.IngredientEntry.COLUMN_NAME_DINNER_ID,dID);
                values.put(GroceryDBContract.IngredientEntry.COLUMN_NAME_GROCERY_ID,ingredient.getID());
                db.insert(GroceryDBContract.IngredientEntry.TABLE_NAME,
                        null,
                        values);
            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
        db.close();
        return 0;
    }

    public int deleteEssentialGrocery(Grocery grocery){
        db = helper.getWritableDatabase();
        String table = GroceryDBContract.GroceryEntry.TABLE_NAME;
        String whereClause = GroceryDBContract.GroceryEntry.COLUMN_NAME_GROCERY_ID+"=?";
        String[] whereArgs = {grocery.getID()+""};
        int affRows = db.delete(table, whereClause, whereArgs);
        db.close();
        return affRows;
    }

    public int deleteDinner(Dinner dinner){
        db = helper.getWritableDatabase();
        String table = GroceryDBContract.DinnerEntry.TABLE_NAME;
        String whereClause = GroceryDBContract.DinnerEntry.COLUMN_NAME_DINNER_ID+"=?";
        String[] whereArgs = {dinner.getID()+""};
        int affRows = db.delete(table,whereClause,whereArgs);
        db.close();
        return affRows;
    }

    private ContentValues getContentValuesForGrocery(Grocery grocery){
        ContentValues values = new ContentValues();
        values.put(GroceryDBContract.GroceryEntry.COLUMN_NAME_GROCERY_ID,grocery.getID());
        values.put(GroceryDBContract.GroceryEntry.COLUMN_NAME_NAME,grocery.getName());
        values.put(GroceryDBContract.GroceryEntry.COLUMN_NAME_PRICE,grocery.getPrice());
        values.put(GroceryDBContract.GroceryEntry.COLUMN_NAME_ORDER,grocery.getOrder());
        values.put(GroceryDBContract.GroceryEntry.COLUMN_NAME_TYPE,grocery.getType());
        values.put(GroceryDBContract.GroceryEntry.COLUMN_NAME_ICON,grocery.getIconUriString());
        return values;
    }

    private ContentValues getContentValuesForDinner(Dinner dinner){
        ContentValues values = new ContentValues();
        values.put(GroceryDBContract.DinnerEntry.COLUMN_NAME_DINNER_ID,dinner.getID());
        values.put(GroceryDBContract.DinnerEntry.COLUMN_NAME_NAME,dinner.getName());
        values.put(GroceryDBContract.DinnerEntry.COLUMN_NAME_ORDER,dinner.getOrder());
        values.put(GroceryDBContract.DinnerEntry.COLUMN_NAME_ICON,dinner.getIconName());
        return values;
    }

    private ContentValues getContentValuesForIngredients(Dinner dinner){

        return null;
    }

    /*--Dinner queries---------------------------------------*/

    /**
     *
     * @param allGroceries, where key is grocery ID of given grocery
     * @return list of dinners pointing to existing grocery instances
     */
    public ArrayList<Dinner> getDinners(HashMap<Integer,Grocery> allGroceries){
        db = helper.getReadableDatabase();
        // Returns all dinners in database
        String[] projection = {"*"};
        Cursor c = db.query(
                GroceryDBContract.DinnerEntry.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                GroceryDBContract.DinnerEntry.COLUMN_NAME_ORDER
        );

        if(c.moveToFirst()==false) return null;
        // Not getting icons/drawables here
        ArrayList<Dinner> allDinners = new ArrayList<>();
        do{
            int ID          = c.getInt(0);
            String name     = c.getString(1);
            String iconName = c.getString(2);
            int order       = c.getInt(3);
            allDinners.add(new Dinner(ID, name, iconName, order));
        } while (c.moveToNext());
        db.close();

        setGroceryPointers(allGroceries, allDinners);
        return allDinners;
    }

    private boolean setGroceryPointers(HashMap<Integer,Grocery> allGroceries, ArrayList<Dinner> allDinners){
        // 1. Order by dinner
        // 2. Loop through dinner and set grocery array

        db = helper.getReadableDatabase();
        Cursor c = db.query(GroceryDBContract.IngredientEntry.TABLE_NAME,
                null, // columns / projection , return all columns
                null, // selection
                null, // selection args
                null, // groupBy
                null, // having
                GroceryDBContract.IngredientEntry.COLUMN_NAME_DINNER_ID
        );
        HashMap<Integer,ArrayList<Integer>> ingredients = new HashMap<>();
        if(!c.moveToFirst()){return false;}
        ArrayList<Integer> groceriesId;
        do{
            int dinnerId = c.getInt(0);
            int groceryId = c.getInt(1);
            groceriesId = ingredients.get(dinnerId);
            if(groceriesId == null){
                groceriesId = new ArrayList<>();
                ingredients.put(dinnerId, groceriesId);
            }
            groceriesId.add(groceryId);
        }while(c.moveToNext());

        // Sort all grocery id's inside given list over ingredients

        for(Integer dinnerId : ingredients.keySet()){
            Collections.sort(ingredients.get(dinnerId));
        }

        // Set pointers
        Collections.sort(allDinners, Dinner.DinnerIdComparator);
        ArrayList<Dinner> popAbleDinnerList = (ArrayList<Dinner>) allDinners.clone();

        for(Integer dinnerId : ingredients.keySet()){
            Dinner dinner;
            do {
                // Only safe if dinners are sorted correctly on id's. This should always hit on first if list is not empty
                dinner = popAbleDinnerList.remove(0);
            }while(dinner != null && dinnerId != dinner.getID());
            // If hit, find all corresponding groceries by id , and set pointer
            if(dinner != null){
                // Set all grocery pointers by id
                for(Integer groceryId : ingredients.get(dinnerId)){
                    // Get grocery by id-key
                    dinner.addIngredient(allGroceries.get(groceryId));
                }
            }
        }
        db.close();
        return true;
    }
}
