package ru.novikov.themoviedb.model.network.tasks;

import android.graphics.Bitmap;

/**
 * Created by inovikov on 13.10.2016.
 */

public class LoadImageTask extends Task {

    private final String mRequestUrl;
    private final String mImageUrl;
    private final int mReqWidth;
    private final int mReqHeight;

    private Bitmap mResponseImage;

    public LoadImageTask(String requestUrl, String imageUrl, int reqWidth, int reqHeight) {
        super(TYPE_REQUEST_GET_IMAGE);
        mRequestUrl = requestUrl;
        mImageUrl = imageUrl;
        mReqWidth = reqWidth;
        mReqHeight = reqHeight;
    }

    @Override
    public Runnable getRunnable() {
        return new Runnable() {
            public void run() {
                mResponseImage = sHttpClient.downloadAndResizeBitmap(mRequestUrl, mReqWidth, mReqHeight);
                sRemoteProvider.handleState(LoadImageTask.this, TASK_COMPLETE, mRequestType, mRequestId);
            }
        };
    }

    public Bitmap getResponseImage() {
        return mResponseImage;
    }

    public String getImageUrl() {
        return mImageUrl;
    }
}
