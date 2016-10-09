package ru.novikov.themoviedb.view;

import java.util.List;

import ru.novikov.themoviedb.model.Entity.Movie;

/**
 * Created by Ivan on 08.10.2016.
 */

public interface MoviesListView extends View{

    void updateList(List<Movie> movieList);

}
