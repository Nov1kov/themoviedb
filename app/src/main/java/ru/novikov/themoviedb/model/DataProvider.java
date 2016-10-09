package ru.novikov.themoviedb.model;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import ru.novikov.themoviedb.model.Entity.Movie;

/**
 * Created by Ivan on 08.10.2016.
 */

public class DataProvider implements RemoteProviderCallBack {

    private RemoteProvider mRemoteProvider;
    private List<DataProviderCallBacks> mDataProviderCallBacksList;

    public DataProvider() {

        mRemoteProvider = new RemoteProvider(this);
        mDataProviderCallBacksList = new ArrayList<>();

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
