package ru.novikov.themoviedb.presenter.basepresenters;

import ru.novikov.themoviedb.model.entity.Movie;

/**
 * Created by Ivan on 08.10.2016.
 */

public interface MovieDetailPresenter extends Presenter {

    void loadMovie(int movieId);
    Movie getCurrentMovie();
    void detachListener();
    void loadBackdrop(String imageUrl, int width, int height);
}
