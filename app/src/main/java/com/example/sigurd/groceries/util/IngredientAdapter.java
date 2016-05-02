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
import java.util.HashMap;

/**
 * Created by Sigurd on 23.12.2015.
 */
public class IngredientAdapter extends BaseAdapter {

    ArrayList<Grocery> ingredients;
    Context context;
    private String TAG = this.getClass().getSimpleName();

    public IngredientAdapter(Context context,ArrayList<Grocery> ingredients){
        setContext(context);
        setIngredients(ingredients);
    }

    public void setContext(Context context){
        this.context = context;
    }

    public void setIngredients(ArrayList<Grocery> ingredients){
        this.ingredients = ingredients;
    }

    @Override
    public int getCount() {
        return ingredients.size();
    }

    @Override
    public Object getItem(int position) {
        return ingredients.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Grocery grocery = ingredients.get(position);
        Log.d(TAG, "Getting view for ingredient");
        if(convertView==null){
            convertView = LayoutInflater.from(context).inflate(R.layout.essential_grocery_view, parent, false);
            Log.d(TAG,"Inflating view");
        }
        fillView(convertView, grocery);
        return convertView;
    }

    private void fillView(View view, Grocery grocery){
        ImageView image = (ImageView) view.findViewWithTag("grocery_image");
        TextView name = (TextView) view.findViewWithTag("grocery_name");
        TextView price = (TextView) view.findViewWithTag("grocery_price");
        TextView duration = (TextView) view.findViewWithTag("grocery_duration");

        image.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        image.setImageDrawable(grocery.getIcon(context));
        name.setText(grocery.getName());
        price.setText("Produkt:pris");
        duration.setText("4-5 dager");
    }

    public ArrayList<Grocery> getIngredients(){
        return ingredients;
    }
}
