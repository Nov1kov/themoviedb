package ru.novikov.themoviedb.model.network.errors;

/**
 * Created by Ivan on 17.10.2016.
 */

public class HttpResponseError extends AppException {

    private int mErrorCode;

    public HttpResponseError(int errorCode) {
        super();
        mErrorCode = errorCode;
    }

    public HttpResponseError(String msg) {
        super(msg);
    }

    public int getErrorCode() {
        return mErrorCode;
    }
}
