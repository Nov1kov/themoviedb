package ru.novikov.themoviedb.model.network.tasks;

import android.graphics.Bitmap;
import android.os.Message;
import android.util.Pair;

/**
 * Created by inovikov on 13.10.2016.
 */

public class GetImageTask extends Task {

    final String requestUrl;
    final String imageUrl;
    final int requestType;
    final int reqWidth;
    final int reqHeight;
    private Bitmap mResponseImage;

    public GetImageTask(String requestUrl, String imageUrl, int reqWidth, int reqHeight) {
        this.requestUrl = requestUrl;
        this.imageUrl = imageUrl;
        this.requestType = TYPE_REQUEST_GET_IMAGE;
        this.reqWidth = reqWidth;
        this.reqHeight = reqHeight;
    }

    @Override
    public Runnable getRunnable() {
        return new Runnable() {
            public void run() {
                mResponseImage = mHttpClient.downloadAndResizeBitmap(requestUrl, reqWidth, reqHeight);

            }
        };;
    }
}
