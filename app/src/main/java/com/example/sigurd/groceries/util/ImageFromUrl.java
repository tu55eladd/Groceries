package com.example.sigurd.groceries.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.webkit.URLUtil;

import java.io.IOException;
import java.net.URL;

/**
 * Created by Sigurd on 29.12.2015.
 */
public class ImageFromUrl {

    private final String TAG = this.getClass().getSimpleName();
    Bitmap bitmap;
    URL imageurl;
    String filename;

    public ImageFromUrl(URL imageurl){
        this.imageurl = imageurl;
        bitmap = loadToBitmap(imageurl);
    }

    private Bitmap loadToBitmap(URL imageurl){
        try {
            return BitmapFactory.decodeStream(imageurl.openConnection().getInputStream());
        } catch (IOException e) {
            //Log.d(TAG, e.printStackTrace());
            return null;
        }
    }






}
