package ru.novikov.themoviedb.model;

import android.graphics.Bitmap;
import android.util.Pair;

import java.util.ArrayList;
import java.util.List;

import ru.novikov.themoviedb.model.entity.Movie;
import ru.novikov.themoviedb.model.network.RemoteProvider;
import ru.novikov.themoviedb.model.network.RemoteProviderCallBack;

/**
 * Created by Ivan on 08.10.2016.
 * Manage all data
 */

public class DataProvider implements RemoteProviderCallBack {

    private final ImagesCache mImagesCache;
    private final ImageLoadListenerController mImageListenerController;
    private RemoteProvider mRemoteProvider;
    private MoviesCache moviesCache;
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
            int requestId = mRemoteProvider.getImage(imageUrl, reqWidth, reqHeight);
            mImageListenerController.put(bitmapListener, requestId);
        } else {
            mImageListenerController.put(bitmapListener, 0);
            bitmapListener.onResponseBitmap(cachedBitmap);
        }

    }

    public void clearListeners() {
        mImageListenerController.clear();
    }

    public void removeImageListener(ImageLoadListenerController.BitmapListener bitmapListener) {
        mImageListenerController.remove(bitmapListener);
    }

    @Override
    public void responseImage(Object obj, int requestId) {
        if (obj instanceof Pair) {
            Pair pair = (Pair) obj;
            String imageUrl = (String) pair.first;
            Bitmap bitmap = (Bitmap) pair.second;
            ImageLoadListenerController.BitmapListener bitmapListener = mImageListenerController.getListener(requestId);
            mImagesCache.put(imageUrl, bitmap);
            if (bitmapListener != null) {
                if (bitmap != null) {
                    bitmapListener.onResponseBitmap(bitmap);
                } else {
                    bitmapListener.onResponseError();
                }
                mImageListenerController.remove(bitmapListener);
            }
        }
    }

    @Override
    public void responseError() {
        for (DataProviderCallBacks subscriber: mDataProviderCallBacksList) {
            subscriber.responseError(null);
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
