package ru.novikov.themoviedb.model.network.tasks;

import org.json.JSONObject;

import java.util.List;

import ru.novikov.themoviedb.model.entity.Movie;
import ru.novikov.themoviedb.model.network.ResponseAdapter;

/**
 * Created by inovikov on 13.10.2016.
 */

public class GetPopularMoviesTask extends Task {

    private final String mPageId;
    private final String mRequestUrl;
    private List<Movie> mMovieList;

    public GetPopularMoviesTask(String requestUrl, String pageId) {
        super(TYPE_REQUEST_GET_POPULAR_MOVIES);
        mRequestUrl = requestUrl;
        mPageId = pageId;
    }

    @Override
    public Runnable getRunnable() {
        return new Runnable() {
            public void run() {
                JSONObject jsonObject = sHttpClient.requestWebService(mRequestUrl);
                mMovieList = ResponseAdapter.parseMoviesList(jsonObject);
                sRemoteProvider.handleState(GetPopularMoviesTask.this, TASK_COMPLETE, mRequestType, mRequestId);
            }
        };
    }

    public String getRequestUrl() {
        return mRequestUrl;
    }

    public int getPageId() {
        return Integer.parseInt(mPageId);
    }

    public List<Movie> getMovieList() {
        return mMovieList;
    }
}
