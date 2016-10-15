package ru.novikov.themoviedb.model.network.tasks;

import org.json.JSONObject;

import ru.novikov.themoviedb.model.entity.Movie;
import ru.novikov.themoviedb.model.network.ResponseAdapter;

/**
 * Created by inovikov on 13.10.2016.
 */

public class GetDetailMovieTask extends Task {

    private final String mMovieId;
    private final String mRequestUrl;
    private Movie mMovie;

    public GetDetailMovieTask(String requestUrl, String movieId) {
        super(TYPE_REQUEST_GET_MOVIE_DETAIL);
        mMovieId = movieId;
        mRequestUrl = requestUrl;
    }

    @Override
    public Runnable getRunnable() {
        return new Runnable() {
            public void run() {
                JSONObject jsonObject = sHttpClient.requestWebService(mRequestUrl);
                mMovie = ResponseAdapter.parseMovieDetail(jsonObject);
                sRemoteProvider.handleState(GetDetailMovieTask.this, TASK_COMPLETE, mRequestType, mRequestId);
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
