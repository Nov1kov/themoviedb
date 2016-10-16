package ru.novikov.themoviedb.presenter.basepresenters;

import ru.novikov.themoviedb.model.DataProviderCallBacks;

/**
 * Created by Ivan on 08.10.2016.
 */

public interface Presenter {

    void setTypeInfoReceiver(@DataProviderCallBacks.TypeInfoDataProvider int typeInfoReceiver);

}
