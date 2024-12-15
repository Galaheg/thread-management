package com.hemre.thread_manager.threads;

import java.util.concurrent.BlockingQueue;

public class SenderThread extends Thread {

    private final BlockingQueue<String> queue;
    private volatile boolean runable = true;
    private final int index;
    private String currentData;
    private boolean priorityChangeable;
    private final String type = "sender";
    private ThreadState threadState = ThreadState.WAITING;



    public SenderThread(BlockingQueue<String> queue, int index, boolean isPriorityChangeable) {
        this.queue = queue;
        this.index = index;
        this.priorityChangeable = isPriorityChangeable;
    }

    @Override
    public void run() {
        threadState =  ThreadState.RUNNING;
        while (runable) {
            try {
                currentData = "Data-" + System.currentTimeMillis();
                queue.put(currentData);
                System.out.println(index + " . Sender added: " + currentData +
                        " works at " + Integer.toString(this.getPriority()) + ". priority");
                Thread.sleep(1000); // 1 saniyede bir veri ekler
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    public SenderThread stopThread() {
        threadState = ThreadState.STOPPED;
        runable = false;
        return this;
    }

    public String getType() {
        return type;
    }

    public boolean isPriorityChangeable() {
        return priorityChangeable;
    }

    public ThreadState getThreadState() {
        return threadState;
    }

    public String getCurrentData() {
        return currentData;
    }

    public int getIndex() {
        return index;
    }


}
