package ru.novikov.themoviedb.model.network.tasks;

import org.json.JSONObject;

import ru.novikov.themoviedb.model.entity.Movie;
import ru.novikov.themoviedb.model.network.ResponseAdapter;
import ru.novikov.themoviedb.model.network.errors.AppException;

/**
 * Created by inovikov on 13.10.2016.
 */

public class DetailMovieTask extends Task {

    private final String mMovieId;
    private final String mRequestUrl;
    private Movie mMovie;

    public DetailMovieTask(String requestUrl, String movieId) {
        super(TYPE_REQUEST_GET_MOVIE_DETAIL);
        mMovieId = movieId;
        mRequestUrl = requestUrl;
    }

    @Override
    public Runnable getRunnable() {
        return new Runnable() {
            public void run() {
                int taskResult = TASK_COMPLETE;
                JSONObject jsonObject = null;
                try {
                    jsonObject = sHttpClient.requestWebService(mRequestUrl);
                } catch (AppException e) {
                    taskResult = TASK_ERROR;
                    mException = e;
                }
                if (jsonObject != null) {
                    mMovie = ResponseAdapter.parseMovieDetail(jsonObject);
                } else {
                    taskResult = TASK_ERROR;
                }
                sRemoteProvider.handleState(DetailMovieTask.this, taskResult, mRequestType, mRequestId);
            }
        };
    }

    public String getRequestUrl() {
        return mRequestUrl;
    }

    public String getMovieId() {
        return mMovieId;
    }

    public Movie getMovie() {
        return mMovie;
    }
}
