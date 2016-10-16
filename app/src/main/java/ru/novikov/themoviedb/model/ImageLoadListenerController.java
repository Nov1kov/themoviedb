package ru.novikov.themoviedb.model;

import android.graphics.Bitmap;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Ivan on 10.10.2016.
 * Contain list with implements callbacks for Image load
 */
public class ImageLoadListenerController {

    private final HashMap<BitmapListener, Integer> mMapListeners;

    public interface BitmapListener {
        void onResponseBitmap(Bitmap bitmap);
        void onResponseBitmapError();
    }

    public ImageLoadListenerController() {

        mMapListeners = new HashMap<>();

    }

    public void put(BitmapListener bitmapListener, int requestId) {
        mMapListeners.put(bitmapListener, requestId);
    }

    public BitmapListener getListener(int requestId) {
        for (Map.Entry<BitmapListener, Integer> e : mMapListeners.entrySet()) {
            BitmapListener key = e.getKey();
            int value = e.getValue();
            if (value == requestId) {
                return key;
            }
        }
        return null;
    }

    public void clear() {
        mMapListeners.clear();
    }

    public void remove(BitmapListener bitmapListener) {
        mMapListeners.remove(bitmapListener);
    }

}
