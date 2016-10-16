package ru.novikov.themoviedb.model.memorycache;

import android.util.LruCache;
import android.util.SparseArray;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import ru.novikov.themoviedb.model.entity.Movie;

/**
 *
 */
public class MoviesCache {

    private class MovieWrapper {

        private Movie mMovie;
        public boolean isDetail = false;
        public int pageId;

        public MovieWrapper(Movie movie) {
            mMovie = movie;
        }

        public void updateMovie(Movie movie) {
            mMovie = movie;
        }

        public Movie getMovie(){
            return mMovie;
        }
    }

    private LinkedHashMap<Integer, MovieWrapper> mCache;

    public MoviesCache(int capacity) {
        mCache = new LinkedHashMap<>(capacity);
    }

    public void putDetailMovie(Movie movie) {
        MovieWrapper movieWrapper = mCache.get(movie.id);
        if (movieWrapper == null) {
            movieWrapper = new MovieWrapper(movie);
        } else {
            movieWrapper.updateMovie(movie);
        }
        movieWrapper.isDetail = true;
        mCache.put(movie.id, movieWrapper);
    }

    /* get only detailed movie */
    public Movie getMovie(int id) {
        MovieWrapper movieWrapper = mCache.get(id);
        return movieWrapper != null && movieWrapper.isDetail ? movieWrapper.getMovie() : null;
    }

    public List<Movie> getMoviesList(int pageId) {
        List<Movie> movieList = new ArrayList<>();
        for (MovieWrapper movieWrapper: mCache.values()) {
            if (movieWrapper.pageId == pageId) {
                movieList.add(movieWrapper.getMovie());
            }
        }
        return movieList;
    }

    public void putMoviesList(List<Movie> movieList, int pageId) {
        for (Movie movie: movieList) {
            if (mCache.get(movie.id) == null) {
                MovieWrapper movieWrapper = new MovieWrapper(movie);
                movieWrapper.pageId = pageId;
                mCache.put(movie.id, movieWrapper);
            }
        }
    }
}
