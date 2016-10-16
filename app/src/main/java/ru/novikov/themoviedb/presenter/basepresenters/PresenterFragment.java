package ru.novikov.themoviedb.presenter.basepresenters;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import ru.novikov.themoviedb.App;
import ru.novikov.themoviedb.R;
import ru.novikov.themoviedb.model.DataProvider;
import ru.novikov.themoviedb.model.DataProviderCallBacks;
import ru.novikov.themoviedb.model.network.RequestFactory;
import ru.novikov.themoviedb.model.network.errors.AppException;
import ru.novikov.themoviedb.model.network.errors.BadJsonException;
import ru.novikov.themoviedb.model.network.errors.BadUrlError;
import ru.novikov.themoviedb.model.network.errors.HttpResponseError;
import ru.novikov.themoviedb.view.baseviews.View;

/**
 * Created by Ivan on 08.10.2016.
 */

public abstract class PresenterFragment<T extends View> extends Fragment implements DataProviderCallBacks, Presenter {

    protected T view;
    @DataProviderCallBacks.TypeInfoDataProvider
    protected int mTypeInfoReceiver;

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
    public void responseError(AppException exception) {

        String errorMessage;
        String titleMsg;

        if (exception instanceof HttpResponseError) {
            titleMsg = getString(R.string.error_title_server, ((HttpResponseError) exception).getErrorCode());
            errorMessage = getString(R.string.error_message_server, RequestFactory.SERVICE_URL);
        } else if (exception instanceof BadUrlError) {
            titleMsg = getString(R.string.error_title_url_invalid);
            errorMessage = getString(R.string.error_msg_url_invalid, ((BadUrlError) exception).getBadUrl());
        } else if (exception instanceof BadJsonException) {
            titleMsg = getString(R.string.error_title_bad_json);
            errorMessage = getString(R.string.error_msg_bad_json, ((BadJsonException) exception).getBadUrl());
        } else {
            titleMsg = getString(R.string.error_connection_title);
            errorMessage = getString(R.string.error_connection_message);
        }

        view.showError(titleMsg, errorMessage);
    }

    @Override
    public void setTypeInfoReceiver(@DataProviderCallBacks.TypeInfoDataProvider int typeInfoReceiver) {
        mTypeInfoReceiver = typeInfoReceiver;
    }
}
