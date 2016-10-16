package ru.novikov.themoviedb.presenter;

import android.content.Context;

import java.util.List;

import ru.novikov.themoviedb.App;
import ru.novikov.themoviedb.model.DataProviderCallBacks;
import ru.novikov.themoviedb.model.entity.Movie;
import ru.novikov.themoviedb.model.network.RemoteProvider;
import ru.novikov.themoviedb.presenter.basepresenters.MoviesListPresenter;
import ru.novikov.themoviedb.presenter.basepresenters.PresenterFragment;
import ru.novikov.themoviedb.view.baseviews.MoviesListView;

/**
 * Created by Ivan on 08.10.2016.
 */

public class MoviesListPresenterImpl extends PresenterFragment<MoviesListView> implements MoviesListPresenter {

    private int mLastPageLoad = RemoteProvider.FIRST_PAGE_ID;
    private String mSearchQuery;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof MoviesListView) {
            view = (MoviesListView) context;
        }
    }

    @Override
    public void loadList() {
        getDataProvider().getPopularMovies(RemoteProvider.FIRST_PAGE_ID);
    }

    @Override
    public void loadSearchList(String query) {
        mSearchQuery = query;
        getDataProvider().getSearchMovies(query, RemoteProvider.FIRST_PAGE_ID);
    }

    @Override
    public void clearListeners() {
        App.getInstance().getDataProvider().clearListeners();
    }

    @Override
    public void loadMore() {
        mLastPageLoad++;
        getContentToList(mLastPageLoad);
    }

    @Override
    public void responseSuccessful(@DataProviderCallBacks.TypeInfoDataProvider int typeInfo,
                                   Movie movie, List<Movie> movies, int pageId, String query) {
        switch (typeInfo) {
            case TYPE_INFO_POPULAR_MOVIES:
            case TYPE_INFO_SEARCH_MOVIES:
                view.updateList(movies);
                if (pageId < mLastPageLoad) {
                    getContentToList(pageId + 1);
                }
                break;
            case DataProviderCallBacks.TYPE_INFO_MOVIE_DETAIL:
                break;
        }
    }

    private void getContentToList(int pageId) {
        if (mSearchQuery != null) {
            getDataProvider().getSearchMovies(mSearchQuery, pageId);
        } else {
            getDataProvider().getPopularMovies(pageId);
        }
    }
}
