package com.example.sigurd.groceries.util;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sigurd.groceries.R;
import com.example.sigurd.groceries.models.Grocery;

import java.util.ArrayList;

/**
 * Created by Sigurd on 12.12.2015.
 */
public class GroceryAdapter extends BaseAdapter {

    private ArrayList<Grocery> essentials;
    private Context context;
    private final String TAG = GroceryAdapter.class.getSimpleName();

    public GroceryAdapter(Context context,ArrayList<Grocery> essentials){
        setContext(context);
        setEssentials(essentials);
    }

    private void setEssentials(ArrayList<Grocery> essentials){
        this.essentials = essentials;
    }

    private void setContext(Context context){
        this.context = context;
    }

    @Override
    public int getCount() {
        return essentials.size();
    }

    @Override
    public Grocery getItem(int position) {
        Log.d(TAG, "Returning a grocery !??!?!?!?!?!?!??!");
        return essentials.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Grocery grocery = essentials.get(position);

        if(convertView==null){
            convertView = LayoutInflater.from(context).inflate(R.layout.essential_grocery_view, parent, false);
        }
        fillViewData(convertView, grocery);
        return convertView;


    }

    private void fillViewData(View customView, Grocery grocery){
        ImageView image = (ImageView) customView.findViewWithTag("grocery_image");
        TextView name = (TextView) customView.findViewWithTag("grocery_name");
        TextView price = (TextView) customView.findViewWithTag("grocery_price");
        TextView duration = (TextView) customView.findViewWithTag("grocery_duration");

        image.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        image.setImageDrawable(grocery.getIcon(context));
        name.setText(grocery.getName());
        price.setText("Produkt:pris");
        duration.setText("4-5 dager");

    }




}
