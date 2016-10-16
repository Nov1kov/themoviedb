package ru.novikov.themoviedb.model.network;

import android.graphics.Bitmap;

import java.util.List;

import ru.novikov.themoviedb.model.entity.Movie;

/**
 * Created by Ivan on 08.10.2016.
 */

public interface RemoteProviderCallBack {

    void responseMovieDetail(Movie movie);
    void responsePopularMovies(List<Movie> movieList, int pageId);
    void responseImage(Bitmap bitmap, String imageUrl, int requestId);
    void responseError();
    void responseSearchMovies(List<Movie> movieList, String searchQuery, int pageId);
}
