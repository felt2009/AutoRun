package com.wfx.autorunner.common;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * ThreadPool tool
 *
 */
public class ThreadPool {
    // Core thread num
    private static final int CORE_POOL_SIZE = 5;

    // Max thread num
    private static final int MAX_POOL_SIZE = 100;

    // Keep alive time
    private static final int KEEP_ALIVE_TIME = 10000;

    // New thread only be created while all core threads are occupied
    private static BlockingQueue<Runnable> mWorkQueue = new ArrayBlockingQueue<Runnable>(10);

    // Thread factory
    private static ThreadFactory mThreadFactory = new ThreadFactory() {
        private final AtomicInteger integer = new AtomicInteger();

        @Override
        public Thread newThread(Runnable r) {
            return new Thread(r, "BaseThreadPool thread:" + integer.getAndIncrement());
        }
    };

    // Thread poll
    private static ThreadPoolExecutor mThreadPool;

    static {
        mThreadPool = new ThreadPoolExecutor(CORE_POOL_SIZE, MAX_POOL_SIZE, KEEP_ALIVE_TIME,
                TimeUnit.SECONDS, mWorkQueue, mThreadFactory);
    }

    /**
     * Obtain a thread and execute Runnable
     * @param runnable Runnable object
     */
    public static void execute(Runnable runnable) {
        mThreadPool.execute(runnable);
    }

}
