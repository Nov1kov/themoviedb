package ru.novikov.themoviedb;

import android.app.Application;

import ru.novikov.themoviedb.model.DataProvider;

/**
 * Created by Ivan on 08.10.2016.
 */

public class App extends Application {

    private static App mInstanceApp;
    private DataProvider mDataProvider;

    public static App getInstance() {
        return mInstanceApp;
    }

    public DataProvider getDataProvider() {
        return mDataProvider;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mDataProvider = new DataProvider();
        mInstanceApp = this;
    }

}
