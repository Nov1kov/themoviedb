package ru.novikov.themoviedb.presenter;

/**
 * Created by Ivan on 08.10.2016.
 */

public interface MovieDetailPresenter extends Presenter {

    void loadMovie(int movieId);

}
