package com.example.sigurd.groceries.models;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;

import com.example.sigurd.groceries.R;

import java.util.ArrayList;
import java.util.Comparator;

/**
 * Created by Sigurd on 18.12.2015.
 */
public class Dinner implements Comparable<Dinner>{

    private ArrayList<Grocery> ingredients = new ArrayList<>();
    private Drawable icon;
    private String name;
    // ID = -1 , item have not yet gotten an id
    private final int ID;
    private String iconName;
    private int order;


    public Dinner(int ID){
        this.ID = ID;
    }

    public Dinner(int ID,  String name, String iconName, int order){
        this.ID = ID;
        setName(name);
        setIconName(iconName);
        setOrder(order);
    }

    public Dinner(ArrayList<Grocery> ingredients, String iconName, String name, int ID) {
        this.ingredients = ingredients;
        this.iconName = iconName;
        this.name = name;
        this.ID = ID;
    }

    public boolean addIngredient(Grocery grocery){
        return ingredients.add(grocery);
    }

    public boolean addIngredients(ArrayList<Grocery> groceries){
        return ingredients.addAll(groceries);
    }

    public double getPrice(){
        double price = 0;
        for(Grocery g : ingredients ){
            price+=g.getPrice();
        }
        return price;
    }

    public void setIngredients(ArrayList<Grocery> ingredients){
        this.ingredients = ingredients;
    }

    public void setIcon(Drawable icon){
        this.icon = icon;
    }

    public Drawable getIcon(Context context){
        if(icon==null) return ContextCompat.getDrawable(context, R.drawable.dinner);
        return icon;
    }

    public void setName(String name){
        this.name = name;
    }

    public String getName(){
        if(name==null) return "Dinner title";
        return name;
    }

    public void setIconName(String iconName){
        this.iconName = iconName;
    }

    public String getIconName(){
        return iconName;
    }

    public int getID(){
        return ID;
    }

    public int getOrder(){
        return order;
    }

    public void setOrder(int order){
        this.order = order;
    }

    public int compareTo(Dinner dinnerToCompare){
        // Lowest first
        return dinnerToCompare.getOrder() - order;
    }

    public static Comparator<Dinner> DinnerIdComparator = new Comparator<Dinner>() {

        public int compare(Dinner d, Dinner d2){
            return d.getID() - d2.getID();
        }
    };

    public ArrayList<Grocery> getIngredients(){
        return ingredients;
    }
}
