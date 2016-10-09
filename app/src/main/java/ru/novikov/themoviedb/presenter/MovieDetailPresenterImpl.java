package ru.novikov.themoviedb.presenter;

import android.content.Context;

import java.util.List;

import ru.novikov.themoviedb.model.entity.Movie;
import ru.novikov.themoviedb.presenter.basepresenters.MovieDetailPresenter;
import ru.novikov.themoviedb.presenter.basepresenters.PresenterFragment;
import ru.novikov.themoviedb.view.baseviews.MovieDetailView;

/**
 * Created by Ivan on 08.10.2016.
 */

public class MovieDetailPresenterImpl extends PresenterFragment<MovieDetailView> implements MovieDetailPresenter {

    private Movie mCurrentMovie;

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
    public Movie getCurrentMovie() {
        return mCurrentMovie;
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void responsePopularMovies(List<Movie> movies) {

    }

    @Override
    public void responseMovieDetail(Movie movie) {
        mCurrentMovie = movie;
        view.updateInfo(movie);
    }

    @Override
    public void responseError(String errorMessage) {

    }
}
