package ru.novikov.themoviedb.model.network.errors;

/**
 * Created by Ivan on 17.10.2016.
 */

public class BadJsonException extends AppException {

    private String mBadUrl;

    public BadJsonException(String badUrl) {
        super();
        mBadUrl = badUrl;
    }

    public String getBadUrl() {
        return mBadUrl;
    }

}
