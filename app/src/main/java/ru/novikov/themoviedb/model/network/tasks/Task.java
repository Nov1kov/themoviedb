package ru.novikov.themoviedb.model.network.tasks;

/**
 * Created by inovikov on 13.10.2016.
 */

public abstract class Task {

    public static final int TYPE_REQUEST_GET_POPULAR_MOVIES = 0;
    public static final int TYPE_REQUEST_GET_MOVIE_DETAIL = 1;
    public static final int TYPE_REQUEST_GET_IMAGE = 2;

    public abstract Runnable getRunnable();

}
