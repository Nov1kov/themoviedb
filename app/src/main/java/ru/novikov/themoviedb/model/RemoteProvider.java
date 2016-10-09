package ru.novikov.themoviedb.model;

import android.os.Handler;
import android.os.Message;

import org.json.JSONObject;

import java.util.Random;

/**
 * send request -> get responses -> convert json to object
 */
public class RemoteProvider {

    public static final String API_KEY = "72b56103e43843412a992a8d64bf96e9";

    public static final String SERVICE_URL = "https://api.themoviedb.org/";
    public static final String IMAGES_URL = "https://image.tmdb.org/t/p/w500/";
    public static final String REST_VERSION = "3/";

    public static final String REST_MOVIE_KEY = "movie/";
    public static final String REST_MOVIE_POPULAR_KEY = "popular";
    public static final String REST_PAGE_QUERY = "&page=";
    public static final String REST_LANGUAGE_QUERY = "&language=";
    public static final String REST_API_KEY = "?api_key=";

    public static final String LANFUAGE_QUERY_EN = "en-US";

    private static final int TYPE_REQUEST_GET_POPULAR_MOVIES = 0;
    private static final int TYPE_REQUEST_GET_MOVIE_DETAIL = 1;
    private final ResponseAdapter mResponseAdapter;

    private Handler mHandler;

    private HttpClient mHttpClient;

    private RemoteProviderCallBack mRemoteProviderListener;

    private final String mLanguage;

    public RemoteProvider(RemoteProviderCallBack remoteProviderListener) {
        mRemoteProviderListener = remoteProviderListener;

        mLanguage = LANFUAGE_QUERY_EN;
        mResponseAdapter = new ResponseAdapter();

        mHttpClient = new HttpClient();
        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.arg1) {
                    case TYPE_REQUEST_GET_POPULAR_MOVIES:
                        mRemoteProviderListener.responsePopularMovies(msg.obj);
                        break;
                    case TYPE_REQUEST_GET_MOVIE_DETAIL:
                        mRemoteProviderListener.responseMovieDetail(msg.obj);
                        break;
                }
            }
        };
    }

    private int generateRequestId() {
        Random r = new Random();
        return r.nextInt(Integer.MAX_VALUE);
    }

    //https://api.themoviedb.org/3/movie/popular?api_key=72b56103e43843412a992a8d64bf96e9&language=en-US&page=1
    public void getPopularMovies(String pageId) {
        runThread(SERVICE_URL + REST_VERSION + REST_MOVIE_KEY + REST_MOVIE_POPULAR_KEY +
                REST_API_KEY + API_KEY + REST_LANGUAGE_QUERY + mLanguage +
                REST_PAGE_QUERY + pageId,
                TYPE_REQUEST_GET_POPULAR_MOVIES,
                generateRequestId());
    }

    public void getMovie(String movieId) {
        runThread(SERVICE_URL + REST_VERSION + REST_MOVIE_KEY + movieId + REST_API_KEY + API_KEY,
                TYPE_REQUEST_GET_MOVIE_DETAIL,
                generateRequestId());
    }

    private void runThread(final String requestUrl, final int requestType, final int requestId) {
        Runnable runnable = new Runnable() {
            public void run() {
                Message msg = mHandler.obtainMessage();
                JSONObject jsonObject = HttpClient.requestWebService(requestUrl);
                if (jsonObject != null) {
                    switch (requestType) {
                        case TYPE_REQUEST_GET_MOVIE_DETAIL:
                            msg.obj = mResponseAdapter.parseMovieDetail(jsonObject);
                            break;
                        case TYPE_REQUEST_GET_POPULAR_MOVIES:
                            msg.obj = mResponseAdapter.parseMoviesList(jsonObject);
                            break;
                    }
                }
                msg.arg1 = requestType;
                msg.arg2 = requestId;
                mHandler.sendMessage(msg);
            }
        };
        Thread thread = new Thread(runnable);
        thread.start();
    }

}
