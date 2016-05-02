package com.example.sigurd.groceries.util;

import android.content.Context;
import android.util.Log;

import com.example.sigurd.groceries.MainActivity;
import com.example.sigurd.groceries.db.GroceryQueries;
import com.example.sigurd.groceries.models.Grocery;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Sigurd on 11.01.2016.
 */
public class Initiator {

    private HashMap<String, ArrayList<Grocery>> groceries;
    private Context context;
    private HashMap<Integer,Grocery> allGroceries = new HashMap<>();
    private final String TAG = Initiator.class.getSimpleName();

    public Initiator(HashMap<String,ArrayList<Grocery>> groceries, Context context){
        this.groceries = groceries;
        this.context = context;
    }

    public HashMap<Integer,Grocery> initiate(){
        initLists();
        getGroceries();
        return allGroceries;
    }

    public void getGroceries() {
        GroceryQueries querier = new GroceryQueries(context);
        ArrayList<Grocery> data = querier.getGroceries();
        if (data != null) {
            Log.d(TAG,"Found grocery data, number of groceries :"+data.size());
            putGroceriesInLists(data);
            return;
        }
        else{
            Log.d(TAG, "Creating test data");
            createTestGroceries();
            putTestGroceriesInDatabase();
        }

    }

    public void createTestGroceries(){
        Log.d(TAG, "Database was empty, using testdata instead");
        groceries.put(MainActivity.ESSENTIALS, new ArrayList<Grocery>());
        groceries.put(MainActivity.NEEDED, new ArrayList<Grocery>());
        groceries.put(MainActivity.FULL, new ArrayList<Grocery>());
        String[] names = {"Milk", "Eggs", "Oatmeal", "Butter", "Bread", "Noodles", "Bananas", "Coffee"};
        for (int i = 0; i < 8; i++) {
            Grocery g = new Grocery(i);
            g.setName(names[i]);
            g.setType(Grocery.NEEDED_ESSENTIAL);
            groceries.get(MainActivity.NEEDED).add(g);
            groceries.get(MainActivity.ESSENTIALS).add(g);
        }
        // For testing if needed has correct behavior
        Grocery notEssential = new Grocery(9);
        notEssential.setType(Grocery.NEEDED_GROCERY);
        groceries.get(MainActivity.NEEDED).add(notEssential);
    }

    public void putTestGroceriesInDatabase(){
        ArrayList<Grocery> needed = groceries.get(MainActivity.NEEDED);
        GroceryQueries querier = new GroceryQueries(context);
        for(Grocery grocery: needed){
            querier.saveGrocery(grocery);
        }
    }

    private void putGroceriesInLists(ArrayList<Grocery> data){
        Log.d(TAG,"Putting groceries in lists");
        ArrayList<Grocery> fullList = groceries.get(MainActivity.FULL);
        ArrayList<Grocery> neededList = groceries.get(MainActivity.NEEDED);
        ArrayList<Grocery> essentialList = groceries.get(MainActivity.ESSENTIALS);
        for (Grocery g : data) {
            allGroceries.put(g.getID(), g);

            //TODO: put into lists according to order in grocery model
            int type = g.getType();
            //Log.d(TAG,"grocery type :"+type);
            if(type == Grocery.NEEDED_ESSENTIAL){
                essentialList.add(g);
                neededList.add(g);
            }
            else if(type == Grocery.FULL_ESSENTIAL){
                essentialList.add(g);
                fullList.add(g);
            }
            else if(type == Grocery.NEEDED_GROCERY){
                neededList.add(g);
            }

        }
    }

    public void initLists(){
        groceries.put(MainActivity.NEEDED, new ArrayList<Grocery>());
        groceries.put(MainActivity.ESSENTIALS, new ArrayList<Grocery>());
        groceries.put(MainActivity.FULL, new ArrayList<Grocery>());
    }
}
