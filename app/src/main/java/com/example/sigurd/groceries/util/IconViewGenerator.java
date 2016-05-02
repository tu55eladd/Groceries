package com.example.sigurd.groceries.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sigurd.groceries.MainActivity;
import com.example.sigurd.groceries.R;
import com.example.sigurd.groceries.models.Grocery;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Sigurd on 10.12.2015.
 */
public class IconViewGenerator {
    public static String TAG = IconViewGenerator.class.getSimpleName();
    public static View generateIconView(final Grocery grocery,Context context){
        View rootView = LayoutInflater.from(context).inflate(R.layout.grocery_icon,null);

        ImageView image = (ImageView) rootView.findViewById(R.id.grocery_image);
        TextView name = (TextView) rootView.findViewById(R.id.grocery_name);

        setGroceryIcon(image,grocery,context);

        name.setText(grocery.getName());

        setNeededFullListner(rootView, grocery,context);

        return rootView;
    }

    private static HashMap<String ,ArrayList<Grocery>> getGroceries(Context context){
        return ((MainActivity) context).groceries;
    }

    private static void setGroceryIcon(ImageView image, Grocery grocery, Context context){

        Bitmap groceryBitmap = grocery.getIconBitmap(context);
        if(groceryBitmap != null){
            image.setImageBitmap(groceryBitmap);
        }
        else{
            Drawable groceryIcon = grocery.getIcon(context);
            image.setImageDrawable(groceryIcon);
        }
        image.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
    }

    private static void setNeededFullListner(View rootView, final Grocery grocery, Context context){
        final HashMap<String, ArrayList<Grocery>> groceries = getGroceries(context);
        // Only changing lists, not actual views, but views are redrawn all the time
        rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<Grocery> essentialsList = groceries.get(MainActivity.ESSENTIALS);
                ArrayList<Grocery> neededList = groceries.get(MainActivity.NEEDED);
                ArrayList<Grocery> fullList = groceries.get(MainActivity.FULL);
                //Log.d(TAG," BEFORE CLICK");
                //Log.d(TAG,MainActivity.ESSENTIALS+" size: "+essentialsList.size());
                //Log.d(TAG,MainActivity.NEEDED+" size: "+neededList.size());
                //Log.d(TAG,MainActivity.FULL+" size: "+fullList.size());
                boolean inEssentials = essentialsList.contains(grocery);
                // Below : Should not be true after dinner ingredient is shopped !
                boolean inNeeded = neededList.contains(grocery);
                boolean full = fullList.contains(grocery);
                if (inNeeded) {
                    neededList.remove(grocery);
                    //Log.d(TAG, "Removing from needed list :"+grocery.hashCode());
                    if (inEssentials) {
                        fullList.add(grocery);
                        grocery.setType(Grocery.FULL_ESSENTIAL);
                        //Log.d(TAG,"Adding to full list"+grocery.hashCode());
                    }
                    else{
                        grocery.setType(Grocery.GROCERY);
                    }
                } else {
                    //Log.d(TAG, "Removing from full list"+grocery.hashCode());
                    fullList.remove(grocery);
                    //Log.d(TAG, "Adding to needed list"+grocery.hashCode());
                    neededList.add(grocery);
                    grocery.setType(Grocery.NEEDED_ESSENTIAL);
                }
                ViewGroup group = (ViewGroup) v.getParent();
                group.removeView(v);
                //Log.d(TAG," AFTER CLICK");
                //Log.d(TAG,MainActivity.ESSENTIALS+" size: "+essentialsList.size());
                //Log.d(TAG,MainActivity.NEEDED+" size: "+neededList.size());
                //Log.d(TAG, MainActivity.FULL + " size: " + fullList.size());
            }
        });
    }
}
