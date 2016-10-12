package ru.novikov.themoviedb.model.network;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by inovikov on 12.10.2016.
 */

public class ThreadManager {

    private static int NUMBER_OF_CORES = Runtime.getRuntime().availableProcessors();
    private final ThreadPoolExecutor mThreadPoolExecutor;

    public ThreadManager() {

        mThreadPoolExecutor = new ThreadPoolExecutor(
                NUMBER_OF_CORES*2,
                NUMBER_OF_CORES*2,
                60L,
                TimeUnit.SECONDS,
                new LinkedBlockingQueue<Runnable>()
        );

    }

    public void addRunnable(Runnable runnable) {
        mThreadPoolExecutor.execute(runnable);
    }
}
