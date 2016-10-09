package ru.novikov.themoviedb.view.baseviews;

import ru.novikov.themoviedb.model.entity.Movie;

/**
 * Created by Ivan on 08.10.2016.
 */

public interface MovieDetailView extends View {

    void updateInfo(Movie movie);

}
