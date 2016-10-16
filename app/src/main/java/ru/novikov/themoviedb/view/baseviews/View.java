package ru.novikov.themoviedb.view.baseviews;

/**
 * Created by Ivan on 08.10.2016.
 */

public interface View {

    /* progress bar */
    void showLoading(boolean shown);
    /* error messages */
    void showError(String msg, String errorMessage);

}
