package com.xaaef.molly.common.fqueue;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

public class FQueueBroadcast {

    private final FQueueRegistry registry;

    private final AtomicLong produced = new AtomicLong();

    public FQueueBroadcast(FQueueRegistry registry) {
        this.registry = registry;
    }

    public Long getProduced() {
        return produced.get();
    }

    public <I> void sendBroadcast(I elm) {
        produced.incrementAndGet();
        FQueueRegistry.sendBroadcast(elm);
    }

    public <I> void sendBroadcast(Class<I> clazz, List<I> elm) {
        produced.addAndGet(elm.size());
        registry.sendBroadcast(clazz, elm);
    }

    public void incrementCounter(int size) {
        produced.addAndGet(size);
    }
}
