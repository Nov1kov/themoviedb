package ru.novikov.themoviedb.model.network.tasks;

import java.util.Random;

import ru.novikov.themoviedb.model.network.HttpClient;
import ru.novikov.themoviedb.model.network.RemoteProvider;

/**
 * Created by inovikov on 13.10.2016.
 */

public abstract class Task {

    public static final int TYPE_REQUEST_GET_POPULAR_MOVIES = 0;
    public static final int TYPE_REQUEST_GET_MOVIE_DETAIL = 1;
    public static final int TYPE_REQUEST_GET_IMAGE = 2;
    public static final int TYPE_REQUEST_GET_SEARCH_MOVIES = 3;

    public static final int TASK_COMPLETE = 0;
    public static final int TASK_ERROR = 1;

    protected final int mRequestType;
    protected final int mRequestId;

    protected static HttpClient sHttpClient = HttpClient.getsInstance();
    protected static RemoteProvider sRemoteProvider = RemoteProvider.getInstance();

    public Task(int requestType) {
        mRequestType = requestType;
        mRequestId = generateRequestId();
    }

    public int getRequestId() {
        return mRequestId;
    }

    public int getRequestType() {
        return mRequestType;
    }

    public abstract Runnable getRunnable();

    private static int generateRequestId() {
        Random r = new Random();
        return r.nextInt(Integer.MAX_VALUE);
    }

}
