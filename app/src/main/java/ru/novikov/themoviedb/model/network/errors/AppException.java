package ru.novikov.themoviedb.model.network.errors;

/**
 * Created by Ivan on 17.10.2016.
 */

public abstract class AppException extends Exception {

    public AppException() {
    }

    public AppException(String msg) {
        super(msg);
    }

    public AppException(String message, Throwable cause) {
        super(message, cause);
    }

    public AppException(Throwable cause) {
        super(cause);
    }

    public AppException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {

    }
}
