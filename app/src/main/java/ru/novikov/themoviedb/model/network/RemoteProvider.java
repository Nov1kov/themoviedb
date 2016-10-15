package ru.novikov.themoviedb.model.network;

import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Pair;

import org.json.JSONObject;

import java.util.List;
import java.util.Random;

import ru.novikov.themoviedb.model.entity.Movie;
import ru.novikov.themoviedb.model.network.tasks.GetDetailMovieTask;
import ru.novikov.themoviedb.model.network.tasks.GetImageTask;
import ru.novikov.themoviedb.model.network.tasks.GetPopularMoviesTask;
import ru.novikov.themoviedb.model.network.tasks.Task;

/**
 * send request -> get responses -> convert json to object
 */
public class RemoteProvider {

    public static final String RELEASE_DATE_FORMAT = "yyyy-MM-dd";
    public static final int MOVIES_PAGE_SIZE = 20;

    private static RemoteProvider sInstance;

    private Handler mHandler;

    private ThreadManager mThreadManager;
    private RequestFactory mRequestFactory;

    private RemoteProviderCallBack mRemoteProviderListener;

    public RemoteProvider(RemoteProviderCallBack remoteProviderListener) {
        mRemoteProviderListener = remoteProviderListener;
        sInstance = this;

        mRequestFactory = new RequestFactory();
        mThreadManager = new ThreadManager();
        HttpClient.getsInstance();

        mHandler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                // for images not show dialog with error
                if (msg.obj == null) {
                    mRemoteProviderListener.responseError();
                    return;
                }
                switch (msg.arg1) {
                    case Task.TYPE_REQUEST_GET_POPULAR_MOVIES:
                        if (msg.obj instanceof GetPopularMoviesTask) {
                            GetPopularMoviesTask popularMoviesTask = (GetPopularMoviesTask) msg.obj;
                            mRemoteProviderListener.responsePopularMovies(popularMoviesTask.getMovieList(),
                                    popularMoviesTask.getPageId());
                        }
                        break;
                    case Task.TYPE_REQUEST_GET_MOVIE_DETAIL:
                        if (msg.obj instanceof GetDetailMovieTask) {
                            GetDetailMovieTask movieDetailTask = (GetDetailMovieTask) msg.obj;
                            mRemoteProviderListener.responseMovieDetail(movieDetailTask.getMovie());
                        }
                        break;
                    case Task.TYPE_REQUEST_GET_IMAGE:
                        if (msg.obj instanceof GetImageTask) {
                            GetImageTask imageTask = (GetImageTask) msg.obj;
                            mRemoteProviderListener.responseImage(imageTask.getResponseImage(),
                                    imageTask.getImageUrl(), msg.arg2);
                        }
                        break;
                }
            }
        };
    }

    public void handleState(Task task, int state, int requestType, int requestId) {
        switch (state) {

            case Task.TASK_COMPLETE:
                Message completeMessage = mHandler.obtainMessage(state, task);
                completeMessage.arg1 = requestType;
                completeMessage.arg2 = requestId;
                completeMessage.sendToTarget();
                break;

        }
    }

    //https://api.themoviedb.org/3/movie/popular?api_key=72b56103e43843412a992a8d64bf96e9&language=en-US&page=1
    public int getPopularMovies(String pageId) {
        GetPopularMoviesTask getImageTask = mRequestFactory.createPopularMoviesTask(pageId);
        mThreadManager.addRunnable(getImageTask.getRunnable());
        return getImageTask.getRequestId();
    }

    public int getMovie(String movieId) {
        GetDetailMovieTask getImageTask = mRequestFactory.createDetailMovieTask(movieId);
        mThreadManager.addRunnable(getImageTask.getRunnable());
        return getImageTask.getRequestId();
    }

    public int getImage(String imageUrl, int reqWidth, int reqHeight) {
        GetImageTask getImageTask = mRequestFactory.createGetImageTask(imageUrl, reqWidth, reqHeight);
        mThreadManager.addRunnable(getImageTask.getRunnable());
        return getImageTask.getRequestId();
    }

    public static RemoteProvider getInstance() {
        return sInstance;
    }
}
