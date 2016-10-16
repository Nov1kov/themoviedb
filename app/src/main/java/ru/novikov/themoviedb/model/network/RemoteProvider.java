package ru.novikov.themoviedb.model.network;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import java.util.List;

import ru.novikov.themoviedb.model.entity.Movie;
import ru.novikov.themoviedb.model.memorycache.MoviesCache;
import ru.novikov.themoviedb.model.network.tasks.DetailMovieTask;
import ru.novikov.themoviedb.model.network.tasks.LoadImageTask;
import ru.novikov.themoviedb.model.network.tasks.PopularMoviesTask;
import ru.novikov.themoviedb.model.network.tasks.SearchTask;
import ru.novikov.themoviedb.model.network.tasks.Task;

/**
 * send request -> get responses -> convert json to object
 */
public class RemoteProvider {

    public static final String RELEASE_DATE_FORMAT = "yyyy-MM-dd";
    public static final int MOVIES_PAGE_SIZE = 20;
    public static final int FIRST_PAGE_ID = 1;
    public static final int CACHED_REQUEST_ID = -1;

    private static RemoteProvider sInstance;

    private Handler mHandler;

    private ThreadManager mThreadManager;
    private RequestFactory mRequestFactory;
    private MoviesCache mMoviesCache;
    private RemoteProviderCallBack mRemoteProviderListener;

    public RemoteProvider(RemoteProviderCallBack remoteProviderListener) {
        sInstance = this;
        mRemoteProviderListener = remoteProviderListener;
        mMoviesCache = new MoviesCache(RemoteProvider.MOVIES_PAGE_SIZE);
        mRequestFactory = new RequestFactory();
        mThreadManager = new ThreadManager();
        HttpClient.getsInstance();

        mHandler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                // for images not show dialog with error
                switch (msg.what) {
                    case Task.TASK_COMPLETE:
                        switch (msg.arg1) {
                            case Task.TYPE_REQUEST_GET_POPULAR_MOVIES:
                                if (msg.obj instanceof PopularMoviesTask) {
                                    PopularMoviesTask popularMoviesTask = (PopularMoviesTask) msg.obj;
                                    mMoviesCache.putMoviesList(popularMoviesTask.getMovieList(),
                                            popularMoviesTask.getPageId());
                                    mRemoteProviderListener.responsePopularMovies(popularMoviesTask.getMovieList(),
                                            popularMoviesTask.getPageId(),
                                            popularMoviesTask.getTotalPages());
                                }
                                break;

                            case Task.TYPE_REQUEST_GET_MOVIE_DETAIL:
                                if (msg.obj instanceof DetailMovieTask) {
                                    DetailMovieTask movieDetailTask = (DetailMovieTask) msg.obj;
                                    mMoviesCache.putDetailMovie(movieDetailTask.getMovie());
                                    mRemoteProviderListener.responseMovieDetail(movieDetailTask.getMovie());
                                }
                                break;

                            case Task.TYPE_REQUEST_GET_IMAGE:
                                if (msg.obj instanceof LoadImageTask) {
                                    LoadImageTask imageTask = (LoadImageTask) msg.obj;
                                    mRemoteProviderListener.responseImage(imageTask.getResponseImage(),
                                            imageTask.getImageUrl(), msg.arg2);
                                }
                                break;

                            case Task.TYPE_REQUEST_GET_SEARCH_MOVIES:
                                if (msg.obj instanceof SearchTask) {
                                    SearchTask searchTask = (SearchTask) msg.obj;
                                    //mMoviesCache.putMoviesList(popularMoviesTask.getMovieList(),
                                            //popularMoviesTask.getPageId());
                                    mRemoteProviderListener.responseSearchMovies(searchTask.getMovieList(),
                                            searchTask.getSearchQuery(),
                                            searchTask.getPageId(),
                                            searchTask.getTotalPages());
                                }
                                break;
                        }
                        break;

                    case Task.TASK_ERROR:
                        mRemoteProviderListener.responseError();
                        break;
                }

            }
        };
    }

    public void handleState(Task task, int state, int requestType, int requestId) {
        Message completeMessage = mHandler.obtainMessage(state, task);
        completeMessage.arg1 = requestType;
        completeMessage.arg2 = requestId;
        completeMessage.sendToTarget();
    }

    //https://api.themoviedb.org/3/movie/popular?api_key=72b56103e43843412a992a8d64bf96e9&language=en-US&page=1
    public int getPopularMovies(int pageId) {

        List<Movie> movieList = mMoviesCache.getMoviesList(pageId);
        if (movieList.size() == RemoteProvider.MOVIES_PAGE_SIZE) {
            mRemoteProviderListener.responsePopularMovies(movieList, pageId, ResponseAdapter.UNDEFINED_TOTAL_PAGES);
            return CACHED_REQUEST_ID;
        } else {
            PopularMoviesTask getImageTask = mRequestFactory.createPopularMoviesTask(Integer.toString(pageId));
            mThreadManager.addRunnable(getImageTask.getRunnable());
            return getImageTask.getRequestId();
        }
    }

    public int getMovie(String movieId) {
        Movie movie = mMoviesCache.getMovie(Integer.parseInt(movieId));
        if (movie != null) {
            mRemoteProviderListener.responseMovieDetail(movie);
            return CACHED_REQUEST_ID;
        } else {
            DetailMovieTask getImageTask = mRequestFactory.createDetailMovieTask(movieId);
            mThreadManager.addRunnable(getImageTask.getRunnable());
            return getImageTask.getRequestId();
        }
    }

    public int getImage(String imageUrl, int reqWidth, int reqHeight) {
        LoadImageTask loadImageTask = mRequestFactory.createGetImageTask(imageUrl, reqWidth, reqHeight);
        mThreadManager.addRunnable(loadImageTask.getRunnable());
        return loadImageTask.getRequestId();
    }

    public int getSearchMovies(String query, int pageId) {
        SearchTask searchTask = mRequestFactory.createSearchMovieTask(query, Integer.toString(pageId));
        mThreadManager.addRunnable(searchTask.getRunnable());
        return searchTask.getRequestId();
    }

    public static RemoteProvider getInstance() {
        return sInstance;
    }
}
