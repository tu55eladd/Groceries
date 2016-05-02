package com.example.sigurd.groceries.models;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;

import com.example.sigurd.groceries.R;

import java.io.IOException;
import java.util.Comparator;

/**
 * Created by Sigurd on 10.12.2015.
 */
public class Grocery implements Comparable<Grocery>{

    public final static int NEEDED_ESSENTIAL = 1;
    public final static int FULL_ESSENTIAL = 2;
    public final static int NEEDED_GROCERY = 3;
    public final static int GROCERY = 4;

    private String name;
    private Drawable groceryIcon;
    private int order;
    private double price;
    private int ID;
    private int type;
    private int iconId = -1;
    private String iconText;
    private Uri iconUri;
    private Bitmap iconBitmap;

    public Grocery(int ID){
        this.ID = ID;

    }
    // ID,name,price,order,type,icon
    public Grocery(int ID, String name,double price , int order, int type, Uri iconUri) {
        setName(name);
        setOrder(order);
        setPrice(price);
        setType(type);
        this.ID = ID;
    }

    public void setId(long ID){
        /* Will become a problem if groceryId > int.MAX */
        this.ID = (int) ID;
    }

    public void setName(String name){
        this.name = name;
    }

    public String getName(){
        //TODO: Remove when testing is done
        if(name==null) return "Melk";
        return name;
    }

    public Drawable getIcon(Context context){
        //TODO: Make this function not require context
        //TODO:Remove default icons , make them dynamic
        if(groceryIcon==null){
            if(iconId==-1){
                // if no icon is set, use milk-icon
                return ContextCompat.getDrawable(context, R.drawable.milk);
            }
            groceryIcon = ContextCompat.getDrawable(context,iconId);
        }
        return groceryIcon;
    }

    public void setIcon(Drawable groceryIcon){

        this.groceryIcon = groceryIcon;
    }

    public void setIcon(int iconId){
        iconId = iconId;
    }

    public void setPrice(double price){
        this.price = price;
    }

    public double getPrice() {
        //TODO: Remove when testing is done
        if(price ==0) return 14.5;
        return price;
    }

    public int getID(){
        return ID;
    }

    public void setOrder(int order){
        this.order = order;
    }

    public int getOrder(){
        return order;
    }

    public String getIconText(){
        return iconText;
    }

    public int getType(){
        return type;
    }

    public void setType(int type){
        this.type = type;
    }

    public int compareTo(Grocery compareGrocery){
        // Lowest first
        return this.getID() - compareGrocery.getID();
        //return  compareGrocery.getOrder() - order;
    }

    public void setIconUri(Uri iconUri){
        this.iconUri = iconUri;
    }

    public Uri getIconUri(){
        return iconUri;
    }

    public String getIconUriString(){
        if(iconUri != null){
            return iconUri.toString();
        }
        return "";
    }

    public Bitmap getIconBitmap(Context context){
        if(iconUri != null){
            if(iconBitmap == null){
                tryToGetIconBitmap(context);
            }
            return iconBitmap;
        }
        return null;

    }

    private void tryToGetIconBitmap(Context context){
        try {
            iconBitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), iconUri);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Comparator<Grocery> GroceryIdComparator = new Comparator<Grocery>() {

        public int compare(Grocery g, Grocery g2){
            return g.getID() - g2.getID();
        }
    };

}
