package ru.novikov.themoviedb.model.network.tasks;

import org.json.JSONObject;

import java.util.List;

import ru.novikov.themoviedb.model.entity.Movie;
import ru.novikov.themoviedb.model.network.ResponseAdapter;

/**
 * Created by inovikov on 13.10.2016.
 */

public class PopularMoviesTask extends Task {

    private final String mPageId;
    private final String mRequestUrl;
    private List<Movie> mMovieList;

    public PopularMoviesTask(String requestUrl, String pageId) {
        super(TYPE_REQUEST_GET_POPULAR_MOVIES);
        mRequestUrl = requestUrl;
        mPageId = pageId;
    }

    @Override
    public Runnable getRunnable() {
        return new Runnable() {
            public void run() {
                JSONObject jsonObject = sHttpClient.requestWebService(mRequestUrl);
                int taskResult = TASK_COMPLETE;
                if (jsonObject != null) {
                    mMovieList = ResponseAdapter.parseMoviesList(jsonObject);
                } else {
                    taskResult = TASK_ERROR;
                }
                sRemoteProvider.handleState(PopularMoviesTask.this, taskResult, mRequestType, mRequestId);
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
