package com.example.sigurd.groceries;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;


import com.example.sigurd.groceries.db.GroceryQueries;
import com.example.sigurd.groceries.fragments.DinnerFragment;
import com.example.sigurd.groceries.fragments.EditDinnerFragment;
import com.example.sigurd.groceries.fragments.EditGroceryFragment;
import com.example.sigurd.groceries.fragments.EssentialsFragment;
import com.example.sigurd.groceries.fragments.MainFragment;
import com.example.sigurd.groceries.models.Dinner;
import com.example.sigurd.groceries.models.Grocery;
import com.example.sigurd.groceries.util.Initiator;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.viewpagerindicator.IconPagerAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeSet;

public class MainActivity extends AppCompatActivity implements EditGroceryFragment.EditGroceryFragmentListener, DinnerFragment.DinnerFragmentListener, EditDinnerFragment.EditDinnerFragmentListener {

    /**
     * The {@link PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link FragmentStatePagerAdapter}.
     */
    private ViewIndicatorAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    // List arguments
    public static final String ESSENTIALS = "essentials";
    public static final String NEEDED = "needed";
    public static final String FULL = "full";

    // Request codes
    public static final int CAPTURE_IMAGE = 100;
    public static final int CROP_PICTUERE = 200;

    //Fragments
    private EssentialsFragment essentialsFragment;
    private MainFragment neededFragment;
    private MainFragment fullFragment;
    private EditGroceryFragment editFragment;
    private DinnerFragment dinnerFragment;
    private EditDinnerFragment editDinnerFragment;

    private static final String TAG = MainActivity.class.getSimpleName();
    public HashMap<String, ArrayList<Grocery>> groceries = new HashMap<>();
    private HashMap<Integer, Grocery> allGroceries;
    private ArrayList<Dinner> dinners;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Initiator initiator = new Initiator(groceries,this);
        allGroceries = initiator.initiate();
        Log.d(TAG, "Creating mainActivity");
        super.onCreate(savedInstanceState);
        //TODO:Get groceries from bundle
/*
        if(savedInstanceState != null){
            restoreInstanceState(savedInstanceState);
        }
        else{
            initGroceries();
        }*/
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new ViewIndicatorAdapter(getSupportFragmentManager());
        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                //TODO:should not redraw if not changed
                if (position == 0) {
                    //getSupportActionBar().setTitle("Needed");
                    neededFragment.redrawGridLayout();
                } else if (position == 1) {
                    //getSupportActionBar().setTitle("Full");
                    fullFragment.redrawGridLayout();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });


        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();

    }

    @Override
    public void onResume(){
        super.onResume();
        Log.d(TAG, "OnResume called");

    }

    @Override
    public void onStart(){
        super.onStart();
        Log.d(TAG, "OnStart called");

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        if (id == R.id.action_essentials) {
            gotoEssentialCustomization();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */


    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        // Save the user's current grocery lists
        //Log.d(TAG,"Saving instance state");
        saveGroceriesToDatabase();
        // Always call the superclass so it can save the view hierarchy state
        super.onSaveInstanceState(savedInstanceState);
    }

    private void saveGroceriesToDatabase(){
        GroceryQueries querier = new GroceryQueries(this);
        querier.updateGroceryStatuses(allGroceries);
    }




    public void restoreInstanceState(Bundle savedInstanceState) {

    }

    public void setEditFragment(EditGroceryFragment editFragment){
        this.editFragment = editFragment;
    }

    private void gotoEssentialCustomization() {

        FragmentManager fm = getSupportFragmentManager();
        if (essentialsFragment == null) {
            essentialsFragment = new EssentialsFragment();
        }
        //fm.beginTransaction().replace(R.id.container,essentialsFragment).addToBackStack(null).commit();
        // Trying to stack fragment on top of the other instead of replacing, and changing container
        fm.beginTransaction().
                add(R.id.viewpager_container, essentialsFragment).
                setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).
                addToBackStack(null).
                commit();
    }

    @Override
    public void onAddNewEssentialGrocery(Grocery grocery) {
        GroceryQueries querier = new GroceryQueries(this);
        long groceryId = querier.saveGrocery(grocery);
        if ( groceryId != -1) {
            grocery.setId(groceryId);
            groceries.get(ESSENTIALS).add(grocery);
            groceries.get(NEEDED).add(grocery);
            onBackPressed();
        }
    }

    @Override
    public void onSaveEssentialGrocery(Grocery updatedGrocery) {
        GroceryQueries querier = new GroceryQueries(this);
        if (querier.updateGrocery(updatedGrocery) > 0) {
            onBackPressed();
        }
        ;
    }

    @Override
    public void onAddDinnerIngredients(Dinner dinner, View view) {
        /* Make sure no ingredients are duplicated */
        ArrayList<Grocery> dinnerIngredients = dinner.getIngredients();
        ArrayList<Grocery> neededList = groceries.get(NEEDED);
        ArrayList<Grocery> fullList = groceries.get(FULL);
        for(Grocery g : dinnerIngredients){
            if(!neededList.contains(g)){
                neededList.add(g);
                // If grocery in essential, which implies full list
                if(fullList.contains(g)){
                    fullList.remove(g);
                    g.setType(Grocery.NEEDED_ESSENTIAL);
                }
                else{
                    g.setType(Grocery.NEEDED_GROCERY);
                }
            }
            else{
                Log.d(TAG,"Ingredient :"+g.getName()+" already in needed list");
            }

        }
        // TODO: where to get not-essential ingredients? and know that they do not duplicate essentials
        messageUser("Dinner "+dinner.getName()+" added", view);
    }

    @Override
    public void onDeleteEssentialGrocery(Grocery groceryToDelete) {
        GroceryQueries querier = new GroceryQueries(this);
        if (querier.deleteEssentialGrocery(groceryToDelete) == 1) {
            onBackPressed();
            groceries.get(ESSENTIALS).remove(groceryToDelete);
            groceries.get(FULL).remove(groceryToDelete);
            groceries.get(NEEDED).remove(groceryToDelete);
        }
    }

    public void messageUser(String message, View view) {
        // View = "clickView"
        Snackbar.make(view, message, Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }

    private void getDinners() {
        if (allGroceries.size() == 0) {
            return;
        }
        GroceryQueries querier = new GroceryQueries(this);
        dinners = querier.getDinners(allGroceries);
    }

    @Override
    public void onAddDinnerView(int dinnerIndex) {
        editDinnerFragment = EditDinnerFragment.newInstance(dinnerIndex);
        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction().
                add(R.id.viewpager_container, editDinnerFragment).
                setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).
                addToBackStack(null).
                commit();
    }

    /*---EditDinnerFragment----*/
    @Override
    public void onGetDinner(int dinnerIndex) {
        editDinnerFragment.setDinner(dinnerFragment.getDinner(dinnerIndex));
    }

    @Override
    public void onSaveDinner(Dinner newDinner) {
        GroceryQueries querier = new GroceryQueries(this);
        querier.saveDinner(newDinner);
        onBackPressed();
    }

    @Override
    public void onUpdateDinner(Dinner newDinner, Dinner oldDinner) {
        GroceryQueries querier = new GroceryQueries(this);
        querier.updateDinner(newDinner, oldDinner);
        onBackPressed();
    }

    @Override
    public void onAddIngredient() {
        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction().
                add(R.id.viewpager_container, MainFragment.newInstance(MainFragment.INGRIDIENTS_FRAGMENT_NUMBER)).
                setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).
                addToBackStack(null).
                commit();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if(requestCode == CAPTURE_IMAGE){
            if(resultCode == RESULT_OK){
                editFragment.onPictureCaptureResult();
            }
        }
        else if (requestCode == CROP_PICTUERE){
            if(resultCode == RESULT_OK){
                editFragment.onCropResult(data);
            }
        }
        else {
            Log.d(TAG, "requestCode : "+requestCode+" responseCode : "+resultCode);
        }
    }

    public ArrayList<Grocery> getFragmentList(int fragmentSectionNumber){
        if(fragmentSectionNumber == MainFragment.NEEDED_FRAGMENT_NUMBER){
            Log.d(TAG,"returning nedded list");
            return groceries.get(NEEDED);
        }
        else if(fragmentSectionNumber == MainFragment.FULL_FRAGMENT_NUMBER){
            Log.d(TAG,"returning full list");
            return groceries.get(FULL);
        }
        else if(fragmentSectionNumber == MainFragment.INGRIDIENTS_FRAGMENT_NUMBER){
            return getPossibleDinnerIngredients();
        }
        Log.d(TAG,"returning null ! sectionNumber : "+fragmentSectionNumber);
        return null;
    }

    private ArrayList<Grocery> getPossibleDinnerIngredients(){
        ArrayList<Grocery> ingredients = new ArrayList<>();
        for(Integer key : allGroceries.keySet()){
            ingredients.add(allGroceries.get(key));
        }
        return ingredients;
    }

    /*----FragmentPagerAdapter----*/

    public class ViewIndicatorAdapter extends FragmentPagerAdapter {


        public ViewIndicatorAdapter(FragmentManager fm) {
            super(fm);

        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.


            if (position == 0) {
                ArrayList<Grocery> neededList = groceries.get(NEEDED);
                neededFragment = MainFragment.newInstance(position + 1);
                return neededFragment;
            } else if (position == 1) {
                ArrayList<Grocery> fullList = groceries.get(FULL);
                fullFragment = MainFragment.newInstance(position + 1);
                return fullFragment;
            } else if (position == 2) {
                dinnerFragment = DinnerFragment.newInstance();
                return dinnerFragment;
            }
            return null;

        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Needed groceries";
                case 1:
                    return "In stock groceries";
                case 2:
                    return "Dinners";
            }
            return null;
        }
    }
}
