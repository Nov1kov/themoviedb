package ru.novikov.themoviedb.presenter.basepresenters;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import ru.novikov.themoviedb.App;
import ru.novikov.themoviedb.model.DataProvider;
import ru.novikov.themoviedb.model.DataProviderCallBacks;
import ru.novikov.themoviedb.view.baseviews.View;

/**
 * Created by Ivan on 08.10.2016.
 */

public abstract class PresenterFragment<T extends View> extends Fragment implements DataProviderCallBacks, Presenter {

    protected T view;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        getDataProvider().subscribe(this);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        view = null;
    }

    @Override
    public void onDestroy() {
        getDataProvider().unsubscribe(this);
        super.onDestroy();
    }

    protected DataProvider getDataProvider() {
        return App.getInstance().getDataProvider();
    }

    @Override
    public void responseError(String errorMessage) {
        view.showError(errorMessage);
    }
}
