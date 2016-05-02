package com.example.sigurd.groceries.util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sigurd.groceries.R;
import com.example.sigurd.groceries.models.Dinner;

import java.util.ArrayList;

/**
 * Created by Sigurd on 18.12.2015.
 */
public class DinnerAdapter extends BaseAdapter{

    private ArrayList<Dinner> dinners;
    private Context context;



    public DinnerAdapter(Context context, ArrayList<Dinner> dinners ){
        this.dinners = dinners;
        this.context = context;
    }

    @Override
    public int getCount(){
        return dinners.size();
    }

    @Override
    public Object getItem(int position) {
        return dinners.get(position);
    }

    @Override
    public long getItemId(int position) {
        //TODO: Pretty sure this is very wrong
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Dinner dinner = dinners.get(position);
        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.dinner_view ,parent,false);
        }
        fillViewData(convertView,dinner);

        return convertView;
    }

    private void fillViewData(View view, Dinner dinner){
        ImageView dinnerIcon = (ImageView) view.findViewById(R.id.dinner_icon);
        TextView dinnerTitle = (TextView) view.findViewById(R.id.dinner_name);
        TextView price = (TextView) view.findViewById(R.id.dinner_price);

        dinnerIcon.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        dinnerIcon.setImageDrawable(dinner.getIcon(context));
        dinnerTitle.setText(dinner.getName());
        price.setText(""+dinner.getPrice()+",-");
    }
}
