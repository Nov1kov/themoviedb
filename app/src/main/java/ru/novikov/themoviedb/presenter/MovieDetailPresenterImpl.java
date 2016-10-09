package ru.novikov.themoviedb.presenter;

import android.content.Context;

import java.util.List;

import ru.novikov.themoviedb.model.Entity.Movie;
import ru.novikov.themoviedb.view.MovieDetailView;
import ru.novikov.themoviedb.view.MoviesListView;

/**
 * Created by Ivan on 08.10.2016.
 */

public class MovieDetailPresenterImpl extends PresenterFragment<MovieDetailView> implements MovieDetailPresenter {

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof MovieDetailView) {
            view = (MovieDetailView) context;
        }
    }

    @Override
    public void loadMovie(int movieId) {
        getDataProvider().getMovie(movieId);
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void responsePopularMovies(List<Movie> movies) {

    }

    @Override
    public void responseMovieDetail(Movie movie) {
        view.updateInfo(movie);
    }

    @Override
    public void responseError(String errorMessage) {

    }
}
