package ru.novikov.themoviedb.model.network;

import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Pair;

import org.json.JSONObject;

import java.util.List;
import java.util.Random;

import ru.novikov.themoviedb.model.entity.Movie;
import ru.novikov.themoviedb.model.network.tasks.Task;

/**
 * send request -> get responses -> convert json to object
 */
public class RemoteProvider {

    public static final String RELEASE_DATE_FORMAT = "yyyy-MM-dd";
    public static final int MOVIES_PAGE_SIZE = 20;

    private final ResponseAdapter mResponseAdapter;

    private Handler mHandler;

    private HttpClient mHttpClient;
    private ThreadManager mThreadManager;
    private RequestFactory mRequestFactory;

    private RemoteProviderCallBack mRemoteProviderListener;

    public RemoteProvider(RemoteProviderCallBack remoteProviderListener) {
        mRemoteProviderListener = remoteProviderListener;

        mRequestFactory = new RequestFactory();
        mThreadManager = new ThreadManager();
        mResponseAdapter = new ResponseAdapter();
        mHttpClient = new HttpClient();

        mHandler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                // for images not show dialog with error
                if (msg.obj == null) {
                    mRemoteProviderListener.responseError();
                    return;
                }
                switch (msg.arg1) {
                    case TYPE_REQUEST_GET_POPULAR_MOVIES:
                        if (msg.obj instanceof Pair) {
                            Pair pair = (Pair) msg.obj;
                            List movieList = (List) pair.first;
                            int pageId = (int) pair.second;
                            mRemoteProviderListener.responsePopularMovies(movieList, pageId);
                        }
                        break;
                    case TYPE_REQUEST_GET_MOVIE_DETAIL:
                        if (msg.obj instanceof Movie) {
                            mRemoteProviderListener.responseMovieDetail((Movie) msg.obj);
                        }
                        break;
                    case TYPE_REQUEST_GET_IMAGE:
                        if (msg.obj instanceof Pair) {
                            Pair pair = (Pair) msg.obj;
                            String imageUrl = (String) pair.first;
                            Bitmap bitmap = (Bitmap) pair.second;
                            mRemoteProviderListener.responseImage(bitmap, imageUrl, msg.arg2);
                        }
                        break;
                }
            }
        };
    }

    public void handleState(Task task, int state) {
        switch (state) {

            // The task finished downloading and decoding the image
            case TASK_COMPLETE:
                /*
                 * Creates a message for the Handler
                 * with the state and the task object
                 */
                Message completeMessage =
                        mHandler.obtainMessage(state, task);
                completeMessage.sendToTarget();
                break;

        }
    }

    //https://api.themoviedb.org/3/movie/popular?api_key=72b56103e43843412a992a8d64bf96e9&language=en-US&page=1
    public int getPopularMovies(String pageId) {
        return sendRequest(mRequestFactory.createUrlPopularMovies(pageId),
                TYPE_REQUEST_GET_POPULAR_MOVIES);
    }

    public int getMovie(String movieId) {
        return sendRequest(mRequestFactory.createUrlMovie(movieId),
                TYPE_REQUEST_GET_MOVIE_DETAIL);
    }

    public int getImage(String imageUrl, int reqWidth, int reqHeight) {
        return sendImageRequest(mRequestFactory.createUrlImage(imageUrl),
                imageUrl,
                TYPE_REQUEST_GET_IMAGE, reqWidth, reqHeight);
    }


    private int sendImageRequest(final String requestUrl, final String imageUrl, final int requestType,
                                 final int reqWidth, final int reqHeight) {
        final int requestId = generateRequestId();
        Runnable runnable = new Runnable() {
            public void run() {
                Message msg = mHandler.obtainMessage();
                if (requestType == TYPE_REQUEST_GET_IMAGE) {
                    msg.obj = Pair.create(imageUrl,
                            mHttpClient.downloadAndResizeBitmap(requestUrl, reqWidth, reqHeight));
                    msg.arg1 = requestType;
                    msg.arg2 = requestId;
                    mHandler.sendMessage(msg);
                }
            }
        };

        mThreadManager.addRunnable(runnable);
        return requestId;
    }

    private int sendRequest(final String requestUrl, final int requestType) {
        final int requestId = generateRequestId();
        Runnable runnable = new Runnable() {
            public void run() {
                Message msg = mHandler.obtainMessage();
                JSONObject jsonObject = mHttpClient.requestWebService(requestUrl);
                if (jsonObject != null) {
                    switch (requestType) {
                        case TYPE_REQUEST_GET_MOVIE_DETAIL:
                            msg.obj = mResponseAdapter.parseMovieDetail(jsonObject);
                            break;
                        case TYPE_REQUEST_GET_POPULAR_MOVIES:
                            msg.obj = Pair.create(mResponseAdapter.parseMoviesList(jsonObject), 1);
                            break;
                    }
                }
                msg.arg1 = requestType;
                msg.arg2 = requestId;
                mHandler.sendMessage(msg);
            }
        };
        mThreadManager.addRunnable(runnable);
        return requestId;
    }

    private int generateRequestId() {
        Random r = new Random();
        return r.nextInt(Integer.MAX_VALUE);
    }
}
