package ru.novikov.themoviedb.presenter.basepresenters;

/**
 * Created by Ivan on 08.10.2016.
 */

public interface MoviesListPresenter extends Presenter {

    void loadList();
    void loadSearchList(String query);
    void clearListeners();
    boolean loadMore();
}
