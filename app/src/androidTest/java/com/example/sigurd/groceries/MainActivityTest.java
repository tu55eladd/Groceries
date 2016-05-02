package com.example.sigurd.groceries;

import android.support.design.widget.FloatingActionButton;
import android.test.ActivityTestCase;
import android.test.TouchUtils;

/**
 * Created by Sigurd on 14.12.2015.
 */
public class MainActivityTest extends ActivityTestCase {

    private MainActivity mainActivity;

    public MainActivityTest(){
        super();
    }

    public void testGotoEssetialsFragment(){
        FloatingActionButton fab = (FloatingActionButton) mainActivity.findViewById(R.id.fab);
        TouchUtils.clickView(this,fab);
    }

    @Override
    public void setUp(){
        mainActivity = (MainActivity) getActivity();
    }
}
