package ru.novikov.themoviedb.view;

import ru.novikov.themoviedb.model.Entity.Movie;

/**
 * Created by Ivan on 08.10.2016.
 */

public interface MovieDetailView extends View {

    void updateInfo(Movie movie);

}
