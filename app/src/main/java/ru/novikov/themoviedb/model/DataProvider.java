package ru.novikov.themoviedb.model;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.util.Pair;

import java.util.ArrayList;
import java.util.List;

import ru.novikov.themoviedb.model.entity.Movie;
import ru.novikov.themoviedb.model.memorycache.ImagesCache;
import ru.novikov.themoviedb.model.memorycache.MoviesCache;
import ru.novikov.themoviedb.model.network.RemoteProvider;
import ru.novikov.themoviedb.model.network.RemoteProviderCallBack;
import ru.novikov.themoviedb.model.network.ResponseAdapter;

/**
 * Created by Ivan on 08.10.2016.
 * Manage all data
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

    private void sendResponseToSubscribers(@DataProviderCallBacks.TypeInfoDataProvider int typeInfo,
                                           Movie movie, List<Movie> movies, int pageId,
                                           String query, int totalPages) {
        for (DataProviderCallBacks subscriber : mDataProviderCallBacksList) {
            subscriber.responseSuccessful(typeInfo, movie, movies, pageId, query, totalPages);
        }
    }

    public void getMovie(int movieId) {
        mRemoteProvider.getMovie(Integer.toString(movieId));
    }

    public void getPopularMovies(int pageId) {
        mRemoteProvider.getPopularMovies(pageId);
    }

    public void getSearchMovies(String query, int pageId) {
        mRemoteProvider.getSearchMovies(query, pageId);
    }

    public void getImage(String imageUrl, int reqWidth, int reqHeight,
                         ImageLoadListenerController.BitmapListener bitmapListener) {
        getImage(imageUrl, reqWidth, reqHeight, bitmapListener, false);
    }

    public void getImage(String imageUrl, int reqWidth, int reqHeight,
                         ImageLoadListenerController.BitmapListener bitmapListener, boolean forced) {

        Bitmap cachedBitmap = mImagesCache.get(imageUrl);
        if (cachedBitmap == null) {
            int requestId = mRemoteProvider.getImage(imageUrl, reqWidth, reqHeight);
            mImageListenerController.put(bitmapListener, requestId);
        } else {
            if (forced) {
                int requestId = mRemoteProvider.getImage(imageUrl, reqWidth, reqHeight);
                mImageListenerController.put(bitmapListener, requestId);
            } else {
                mImageListenerController.put(bitmapListener, RemoteProvider.CACHED_REQUEST_ID);
            }
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
    public void responseImage(Bitmap bitmap, String imageUrl, int requestId) {
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

    @Override
    public void responseError() {
        for (DataProviderCallBacks subscriber: mDataProviderCallBacksList) {
            subscriber.responseError(null);
        }
    }

    @Override
    public void responseSearchMovies(List<Movie> movieList, String searchQuery, int pageId, int totalPages) {
        sendResponseToSubscribers(DataProviderCallBacks.TYPE_INFO_SEARCH_MOVIES,
                null, movieList, pageId, searchQuery, totalPages);
    }

    @Override
    public void responsePopularMovies(@NonNull List<Movie> movieList, int pageId, int totalPages) {
        sendResponseToSubscribers(DataProviderCallBacks.TYPE_INFO_POPULAR_MOVIES,
                null, movieList, pageId, null, totalPages);
    }

    @Override
    public void responseMovieDetail(@NonNull Movie movie) {
        sendResponseToSubscribers(DataProviderCallBacks.TYPE_INFO_MOVIE_DETAIL,
                movie, null, DataProviderCallBacks.UNDEFINED_PAGE_ID, null, ResponseAdapter.UNDEFINED_TOTAL_PAGES);
    }
}
