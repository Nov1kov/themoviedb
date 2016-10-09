package ru.novikov.themoviedb.view.baseviews;

/**
 * Created by Ivan on 08.10.2016.
 */

public interface View {

    void showLoading(boolean shown);
    void showError(String msg);

}
