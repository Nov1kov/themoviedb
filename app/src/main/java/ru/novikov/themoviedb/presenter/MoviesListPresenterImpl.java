package ru.novikov.themoviedb.presenter;

import android.content.Context;

import java.util.List;

import ru.novikov.themoviedb.App;
import ru.novikov.themoviedb.model.entity.Movie;
import ru.novikov.themoviedb.presenter.basepresenters.MoviesListPresenter;
import ru.novikov.themoviedb.presenter.basepresenters.PresenterFragment;
import ru.novikov.themoviedb.view.baseviews.MoviesListView;

/**
 * Created by Ivan on 08.10.2016.
 */

public class MoviesListPresenterImpl extends PresenterFragment<MoviesListView> implements MoviesListPresenter {

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof MoviesListView) {
            view = (MoviesListView) context;
        }
    }

    @Override
    public void loadList() {
        getDataProvider().getPopularMovies(1);
    }

    @Override
    public void clearListeners() {
        App.getInstance().getDataProvider().clearListeners();
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void responsePopularMovies(List<Movie> movies) {
        view.updateList(movies);
    }

    @Override
    public void responseMovieDetail(Movie movie) {

    }

    @Override
    public void responseError(String errorMessage) {

    }
}
