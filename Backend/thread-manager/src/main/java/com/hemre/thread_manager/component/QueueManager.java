package com.hemre.thread_manager.component;

import org.springframework.stereotype.Component;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

@Component
public class QueueManager {

    private final BlockingQueue<String> queue = new LinkedBlockingQueue<>();

    public BlockingQueue<String> getQueue() {
        return queue;
    }
}