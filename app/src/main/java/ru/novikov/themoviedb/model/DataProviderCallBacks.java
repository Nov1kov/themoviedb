package ru.novikov.themoviedb.model;

import java.util.List;

import ru.novikov.themoviedb.model.Entity.Movie;

/**
 * Created by Ivan on 09.10.2016.
 */

public interface DataProviderCallBacks {

    void responsePopularMovies(List<Movie> movies);
    void responseMovieDetail(Movie movie);
    void responseError(String errorMessage);

}
