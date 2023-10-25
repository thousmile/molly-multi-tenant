package com.xaaef.molly.common.util;

import lombok.extern.slf4j.Slf4j;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Consumer;


@Slf4j
public class BatchQueueUtils<T> {

    private final int maxSize;

    private final long timeoutInMs;

    private final Consumer<List<T>> consumer;

    private final ExecutorService executorService;

    private final AtomicBoolean isLooping = new AtomicBoolean(false);

    private final BlockingQueue<T> queue = new LinkedBlockingQueue<>();

    private final AtomicLong start = new AtomicLong(System.currentTimeMillis());


    public static <T> BatchQueueUtils<T> bufferRate(int maxSize, Duration timeoutInMs, Consumer<List<T>> consumer) {
        var es = Executors.newSingleThreadExecutor();
        return bufferRate(maxSize, timeoutInMs, es, consumer);
    }


    public static <T> BatchQueueUtils<T> bufferRate(int maxSize, Duration timeoutInMs, ExecutorService es, Consumer<List<T>> consumer) {
        return new BatchQueueUtils<T>(maxSize, timeoutInMs, es, consumer);
    }


    /**
     * @param maxSize     缓冲最大数量
     * @param timeoutInMs 最小间隔（毫秒）
     * @param consumer
     */
    public BatchQueueUtils(int maxSize, Duration timeoutInMs, ExecutorService es, Consumer<List<T>> consumer) {
        this.maxSize = maxSize;
        this.timeoutInMs = timeoutInMs.toMillis();
        this.executorService = es;
        this.consumer = consumer;
    }


    public boolean add(T t) {
        var result = queue.add(t);
        if (!isLooping.get() && result) {
            isLooping.set(true);
            startLoop();
        }
        return result;
    }


    public void completeAll() {
        while (!queue.isEmpty()) {
            drainToConsume();
        }
    }


    private void startLoop() {
        executorService.execute(() -> {
            start.set(System.currentTimeMillis());
            while (true) {
                long last = System.currentTimeMillis() - start.get();
                if (queue.size() >= maxSize || (!queue.isEmpty() && last > timeoutInMs)) {
                    drainToConsume();
                } else if (queue.isEmpty()) {
                    isLooping.set(false);
                    break;
                }
            }
        });
    }


    private void drainToConsume() {
        var drained = new ArrayList<T>();
        int num = queue.drainTo(drained, maxSize);
        if (num > 0) {
            consumer.accept(drained);
            start.set(System.currentTimeMillis());
        }
    }


}