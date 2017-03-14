package edu.sjsu.yduan.resume;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Marker;
import com.squareup.picasso.Target;
import com.squareup.picasso.Picasso;

public class POITarget implements Target {
    private Marker m;
    public POITarget(Marker m) { this.m = m; }

    @Override public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
        m.setIcon(BitmapDescriptorFactory.fromBitmap(bitmap));
    }

    @Override public void onBitmapFailed(Drawable errorDrawable) {
        m.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.ic_placeholder_pin));
    }

    @Override public void onPrepareLoad(Drawable placeHolderDrawable) {
    }
}
