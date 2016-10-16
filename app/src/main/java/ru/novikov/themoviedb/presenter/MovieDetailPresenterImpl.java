package ru.novikov.themoviedb.presenter;

import android.content.Context;
import android.graphics.Bitmap;

import java.util.List;

import ru.novikov.themoviedb.model.DataProviderCallBacks;
import ru.novikov.themoviedb.model.ImageLoadListenerController;
import ru.novikov.themoviedb.model.entity.Movie;
import ru.novikov.themoviedb.presenter.basepresenters.MovieDetailPresenter;
import ru.novikov.themoviedb.presenter.basepresenters.PresenterFragment;
import ru.novikov.themoviedb.view.baseviews.MovieDetailView;

/**
 * Created by Ivan on 08.10.2016.
 */

public class MovieDetailPresenterImpl extends PresenterFragment<MovieDetailView>
        implements MovieDetailPresenter, ImageLoadListenerController.BitmapListener {

    private Movie mCurrentMovie;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof MovieDetailView) {
            view = (MovieDetailView) context;
        }
    }

    @Override
    public void loadMovie(int movieId) {
        if (mCurrentMovie == null) {
            getDataProvider().getMovie(movieId);
        } else {
            view.updateInfo(mCurrentMovie);
        }
    }

    @Override
    public Movie getCurrentMovie() {
        return mCurrentMovie;
    }

    @Override
    public void detachListener() {
        getDataProvider().removeImageListener(this);
    }

    @Override
    public void loadBackdrop(String imageUrl, int width, int height) {
        getDataProvider().getImage(imageUrl, width, height, this);
    }

    @Override
    public void onResponseBitmap(Bitmap bitmap) {
        view.updateBackdrop(bitmap);
    }

    @Override
    public void onResponseError() {

    }

    @Override
    public void responseSuccessful(@DataProviderCallBacks.TypeInfoDataProvider int typeInfo,
                                   Movie movie, List<Movie> movies, int pageId, String query) {
        switch (typeInfo) {
            case DataProviderCallBacks.TYPE_INFO_MOVIE_DETAIL:
                mCurrentMovie = movie;
                view.updateInfo(movie);
                break;
            case DataProviderCallBacks.TYPE_INFO_POPULAR_MOVIES:
            case DataProviderCallBacks.TYPE_INFO_SEARCH_MOVIES:
                break;
        }
    }
}
