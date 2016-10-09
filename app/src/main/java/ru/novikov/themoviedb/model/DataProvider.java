package ru.novikov.themoviedb.model;

import android.graphics.Bitmap;
import android.util.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import ru.novikov.themoviedb.model.entity.Movie;
import ru.novikov.themoviedb.model.network.RemoteProvider;
import ru.novikov.themoviedb.model.network.RemoteProviderCallBack;

/**
 * Created by Ivan on 08.10.2016.
 */

public class DataProvider implements RemoteProviderCallBack {

    private final ImagesCache mImagesCache;
    private final ImageLoadListenerController mImageListenerController;
    private RemoteProvider mRemoteProvider;
    private List<DataProviderCallBacks> mDataProviderCallBacksList;

    public DataProvider() {

        mRemoteProvider = new RemoteProvider(this);
        mDataProviderCallBacksList = new ArrayList<>();
        mImagesCache = new ImagesCache();
        mImageListenerController = new ImageLoadListenerController();

    }

    public void subscribe(DataProviderCallBacks dataProviderCallBacks) {
        mDataProviderCallBacksList.add(dataProviderCallBacks);
    }

    public void unsubscribe(DataProviderCallBacks dataProviderCallBacks) {
        mDataProviderCallBacksList.remove(dataProviderCallBacks);
    }

    public void getMovie(int movieId) {
        mRemoteProvider.getMovie(Integer.toString(movieId));
    }

    public void getPopularMovies(int pageId) {
        mRemoteProvider.getPopularMovies(Integer.toString(pageId));
    }

    public void getImage(String imageUrl, int reqWidth, int reqHeight,
                         ImageLoadListenerController.BitmapListener bitmapListener) {

        Bitmap cachedBitmap = mImagesCache.get(imageUrl);
        if (cachedBitmap == null) {
            int requestId = mRemoteProvider.loadImage(imageUrl, reqWidth, reqHeight);
            mImageListenerController.put(bitmapListener, requestId);
        } else {
            mImageListenerController.put(bitmapListener, 0);
            bitmapListener.onResponseBitmap(cachedBitmap);
        }

    }

    public void clearListeners() {
        mImageListenerController.clear();
    }

    @Override
    public void responseImage(Object obj, int requestId) {
        if (obj instanceof Pair) {
            Pair pair = (Pair) obj;
            String imageUrl = (String) pair.first;
            Bitmap bitmap = (Bitmap) pair.second;
            mImagesCache.put(imageUrl, bitmap);
            ImageLoadListenerController.BitmapListener bitmapListener = mImageListenerController.getListener(requestId);
            if (bitmapListener != null) {
                bitmapListener.onResponseBitmap(bitmap);
            }
        }
    }

    @Override
    public void responseMovieDetail(Object obj) {
        Movie movie = (Movie) obj;
        for (DataProviderCallBacks subscriber: mDataProviderCallBacksList) {
            subscriber.responseMovieDetail(movie);
        }
    }

    @Override
    public void responsePopularMovies(Object obj) {
        List movieList = (List) obj;
        for (DataProviderCallBacks subscriber: mDataProviderCallBacksList) {
            subscriber.responsePopularMovies(movieList);
        }
    }


}
