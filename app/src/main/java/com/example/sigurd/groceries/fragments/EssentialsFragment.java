package com.example.sigurd.groceries.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.sigurd.groceries.MainActivity;
import com.example.sigurd.groceries.util.GroceryAdapter;
import com.example.sigurd.groceries.R;
import com.example.sigurd.groceries.models.Grocery;

import java.util.ArrayList;

public class EssentialsFragment extends Fragment {

    private ListView essentialsListView;
    private static final String TAG = EssentialsFragment.class.getSimpleName();
    private ArrayList<Grocery> essentials;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "Container: " + getActivity().getResources().getResourceEntryName(container.getId()));
        Log.d(TAG, "Container H:W: "+container.getHeight()+":"+container.getWidth());
        View rootView = inflater.inflate(R.layout.essential_list, container, false);
        // Parent is null ?
        //Log.d(TAG, "Parent of listView : "+rootView.getParent().getClass().getSimpleName());
        MainActivity activity = (MainActivity) getActivity();
        getEssentials(activity);
        setButtonListener(rootView);
        //essentialsListView = (ListView) rootView.findViewById(R.id.essentialList);
        essentialsListView = (ListView) rootView.findViewById(R.id.essentialList);
        fillListView(essentialsListView);
        setItemListener(essentialsListView);

        return rootView;
    }

    private ArrayList<Grocery> getEssentials(MainActivity activity){
        essentials = activity.groceries.get(activity.ESSENTIALS);
        //TODO: Get real objects, maybe from intent?
        ArrayList<Grocery> list = new ArrayList<>();
        for(int i =0; i<4;i++){
            list.add(new Grocery(i));
        }
        return list;
    }

    private void fillListView(ListView list){
        GroceryAdapter adapter = new GroceryAdapter(list.getContext(),essentials);
        list.setAdapter(adapter);
    }

    private void setItemListener(ListView list){
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //open dialog or new screen with grocery as parameter
                Log.d(TAG,"List clicked position : "+position);
                editEssentialGrocery(position);
            }
        });
    }

    private void setButtonListener(View rootView){
        //Same button for both views, should only be in essentialEditing
        FloatingActionButton fab = (FloatingActionButton) rootView.findViewById(R.id.add_essential);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editEssentialGrocery(-1);
                /*
                Snackbar.make(view, "Inside essential fragment", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/
            }
        });
    }

    private void editEssentialGrocery(int groceryIndex){
        //TODO:Move whole method to activity
        FragmentManager fm = ( getActivity()).getSupportFragmentManager();
        fm.beginTransaction().
                add(R.id.viewpager_container, EditGroceryFragment.newInstance(groceryIndex)).
                setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).
                addToBackStack(null).
                commit();
    }
}
