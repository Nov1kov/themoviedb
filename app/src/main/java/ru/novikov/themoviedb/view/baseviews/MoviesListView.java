package ru.novikov.themoviedb.view.baseviews;

import java.util.List;

import ru.novikov.themoviedb.model.entity.Movie;

/**
 * Created by Ivan on 08.10.2016.
 */

public interface MoviesListView extends View{

    void updateList(List<Movie> movieList);

}
