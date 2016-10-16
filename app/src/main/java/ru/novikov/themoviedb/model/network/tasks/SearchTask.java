package ru.novikov.themoviedb.model.network.tasks;

import org.json.JSONObject;

import java.util.List;

import ru.novikov.themoviedb.model.entity.Movie;
import ru.novikov.themoviedb.model.network.ResponseAdapter;

/**
 * Created by Ivan on 16.10.2016.
 */

public class SearchTask extends Task {

    private final String mPageId;
    private final String mRequestUrl;
    private final String mSearchQuery;
    private List<Movie> mMovieList;

    public SearchTask(String urlSearchMovies, String query, String pageId) {
        super(TYPE_REQUEST_GET_SEARCH_MOVIES);
        mRequestUrl = urlSearchMovies;
        mPageId = pageId;
        mSearchQuery = query;
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
                sRemoteProvider.handleState(SearchTask.this, taskResult, mRequestType, mRequestId);
            }
        };
    }

    public List<Movie> getMovieList() {
        return mMovieList;
    }

    public int getPageId() {
        return Integer.parseInt(mPageId);
    }

    public String getSearchQuery() {
        return mSearchQuery;
    }
}