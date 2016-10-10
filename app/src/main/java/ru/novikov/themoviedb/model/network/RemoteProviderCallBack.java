package ru.novikov.themoviedb.model.network;

/**
 * Created by Ivan on 08.10.2016.
 */

public interface RemoteProviderCallBack {

    void responseMovieDetail(Object obj);
    void responsePopularMovies(Object obj);
    void responseImage(Object obj, int requestId);
    void responseError();

}
