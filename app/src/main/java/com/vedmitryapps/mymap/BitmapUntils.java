package com.vedmitryapps.mymap;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.View;

import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;

import java.io.ByteArrayOutputStream;

public class BitmapUntils {

    public static BitmapDescriptor getBitmapFromByteArray(byte[] array){
        return BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeByteArray(array, 0, array.length));
    }

    public static Bitmap getBitmapFromView(View view, int x, int y) {
        Bitmap returnedBitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(returnedBitmap);
        Drawable bgDrawable = view.getBackground();
        if (bgDrawable!=null)
            bgDrawable.draw(canvas);
        else
            canvas.drawColor(Color.WHITE);
        view.draw(canvas);

        ByteArrayOutputStream blob = new ByteArrayOutputStream();
        returnedBitmap.compress(Bitmap.CompressFormat.PNG, 0 /* Ignored for PNGs */, blob);
        return Bitmap.createScaledBitmap(returnedBitmap, x,y, true);
    }

    public static byte[] getBytesFromBitmap(Bitmap returnedBitmap){
        ByteArrayOutputStream blob = new ByteArrayOutputStream();
        returnedBitmap.compress(Bitmap.CompressFormat.PNG, 0 /* Ignored for PNGs */, blob);
        return blob.toByteArray();
    }
}
