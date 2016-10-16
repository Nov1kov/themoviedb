package ru.novikov.themoviedb.model;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.List;

import ru.novikov.themoviedb.model.entity.Movie;
import ru.novikov.themoviedb.model.network.errors.AppException;


/**
 * Created by Ivan on 09.10.2016.
 */

public interface DataProviderCallBacks {

    int UNDEFINED_PAGE_ID = -1;

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({TYPE_INFO_MOVIE_DETAIL, TYPE_INFO_POPULAR_MOVIES, TYPE_INFO_SEARCH_MOVIES})
    public @interface TypeInfoDataProvider {}
    int TYPE_INFO_MOVIE_DETAIL = 0;
    int TYPE_INFO_POPULAR_MOVIES = 1;
    int TYPE_INFO_SEARCH_MOVIES = 2;

    void responseSuccessful(@TypeInfoDataProvider int typeInfo,
                            Movie movie,
                            List<Movie> movies,
                            int pageId,
                            String query,
                            int totalPages);
    void responseError(AppException exception);

}
