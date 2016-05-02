package com.example.sigurd.groceries.fragments;

import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Layout;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.ScrollView;

import com.example.sigurd.groceries.MainActivity;
import com.example.sigurd.groceries.R;
import com.example.sigurd.groceries.models.Grocery;
import com.example.sigurd.groceries.util.IconViewGenerator;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Sigurd on 10.12.2015.
 */
public  class MainFragment extends Fragment{

    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    public static final String ARG_SECTION_NUMBER = "section_number";
    public static final int FULL_FRAGMENT_NUMBER = 2;
    public static final int NEEDED_FRAGMENT_NUMBER = 1;
    public static final int INGRIDIENTS_FRAGMENT_NUMBER = 3;

    private ArrayList<Grocery> fragmentList;
    private static final String TAG = MainFragment.class.getSimpleName();
    private View rootView;
    private GridLayout grid;
    private static Rect measuredRect;


    public MainFragment() {

    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static MainFragment newInstance(int sectionNumber) {
        MainFragment fragment = new MainFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG,"Creating mainfragment view");
        Bundle instanceState = savedInstanceState != null ? savedInstanceState : getArguments();
        setInstanceState(instanceState);

        this.rootView = inflater.inflate(R.layout.fragment_main, container, false);
        this.grid = (GridLayout) rootView.findViewById(R.id.groceryGrid);
        queueViewFilling(rootView);
        return rootView;

    }

    private void setInstanceState(Bundle args){

        getFragmentList(args);
    }

    private void getFragmentList(Bundle savedInstanceState){
        int fragmentSectionNumber = savedInstanceState.getInt(ARG_SECTION_NUMBER);
        fragmentList = ((MainActivity)getActivity()).getFragmentList(fragmentSectionNumber);
    }

    @Override
    public void onSaveInstanceState(Bundle outState){
        //Log.d(TAG, "Saving instance state");
        outState.putInt(ARG_SECTION_NUMBER,getArguments().getInt(ARG_SECTION_NUMBER));
        super.onSaveInstanceState(outState);
    }

    private void setColumnSize(Rect windowRect , GridLayout grid ){
        double windowHeight = windowRect.bottom - windowRect.top;
        double windowWidth = windowRect.right - windowRect.left;
        //Log.d(TAG, "Screen width: " + windowWidth);
        //Log.d(TAG, "Screen height: " + windowHeight);
        grid.setColumnCount(2);
        int listSize = fragmentList != null ? fragmentList.size() : 0;
        int rowCount = (int)Math.ceil((listSize+1)/2);
        //Log.d(TAG, "Rowcount : " + rowCount);

        grid.setRowCount(rowCount);

    }

    private void fillGrid(Rect windowRect ,GridLayout grid){
        ArrayList<Grocery> fragmentListCopy = (ArrayList<Grocery>) fragmentList.clone();
        Context context = getContext();
        //Log.d(TAG,"Possible squares : "+ (grid.getColumnCount()*grid.getRowCount()));
        //Log.d(TAG, "List size: "+fragmentListCopy.size());
        //Need to do it this way to get column and row numbers
        for(int row=0; row<grid.getRowCount(); row++){
            for(int col=0; col<grid.getColumnCount();col++){
                // Create view for grocery icon
                if(fragmentListCopy.size()==0){
                    //Log.d(TAG, "Breaking because list is empty");
                    break;
                }
                GridLayout.LayoutParams layout = specifyLayout(row,col,windowRect);
                Grocery grocery = fragmentListCopy.remove(0);
                View icon = IconViewGenerator.generateIconView(grocery, context);
                grid.addView(icon, layout);
            }
        }
        //Log.d(TAG, "Gridlayout height: " + grid.getHeight());
        //Log.d(TAG, "Scrollview height: "+((ScrollView) grid.getParent()).getHeight());
    }

    private Rect getScreenRect(View rootView){
        if (measuredRect != null) return measuredRect;
        Rect windowRect = new Rect();
        //TODO: Find correct window size
        ScrollView scrollView = (ScrollView) rootView.findViewById(R.id.groceryGrid).getParent();
        scrollView.getGlobalVisibleRect(windowRect);
        measuredRect = windowRect;
        return measuredRect;
    }

    private GridLayout.LayoutParams specifyLayout(int row, int col,Rect windowRect){
        GridLayout.Spec rowSpec = GridLayout.spec(row);
        GridLayout.Spec colSpec = GridLayout.spec(col);
        GridLayout.LayoutParams layout = new GridLayout.LayoutParams(rowSpec,colSpec);
        layout.width = windowRect.width()/2;
        layout.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        layout.setGravity(Gravity.CENTER);
        return layout;
    }

    private void queueViewFilling(View rootView){
        final View v = rootView.findViewById(R.id.scrollView);
        v.post(new Runnable() {
            @Override
            public void run() {
                redrawGridLayout();
            }
        });

    }

    public void redrawGridLayout(){
        //Remove children first
        if(grid.getChildCount()!=0){
            grid.removeAllViews();
        }
        Rect windowRect = getScreenRect(rootView);
        setColumnSize(windowRect, grid);
        fillGrid(windowRect, grid);
    }



}
