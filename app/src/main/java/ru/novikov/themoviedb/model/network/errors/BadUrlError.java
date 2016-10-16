package ru.novikov.themoviedb.model.network.errors;

/**
 * Created by Ivan on 17.10.2016.
 */

public class BadUrlError extends AppException {

    private String mBadUrl;

    public BadUrlError(String badUrl) {
        super();
        mBadUrl = badUrl;
    }

    public String getBadUrl() {
        return mBadUrl;
    }
}
