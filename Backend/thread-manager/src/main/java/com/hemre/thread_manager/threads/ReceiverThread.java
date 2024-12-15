package com.hemre.thread_manager.threads;

import java.util.concurrent.BlockingQueue;

public class ReceiverThread extends AbstractThread {

    private final BlockingQueue<String> queue;
    private final int index;
    private volatile boolean runable = true;
    private boolean priorityChangeable;
    private String currentData;
    private final String type = "receiver";
    private ThreadState threadState = ThreadState.WAITING;


    public ReceiverThread(BlockingQueue<String> queue, int index, boolean priorityChangable) {
        this.queue = queue;
        this.index = index;
        this.priorityChangeable = priorityChangable;
    }

    @Override
    public void run() {
        threadState = ThreadState.RUNNING;

        while (runable) {
            try {
                currentData = queue.take();
                System.out.println(index + ". Receiver processed: " + currentData +
                        " works at " + Integer.toString(this.getPriority()) + ". priority");
                Thread.sleep(1000); // 1 saniyede bir veri tüketir
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    public ReceiverThread stopThread() {
        threadState = ThreadState.STOPPED;
        runable = false;
        return this;
    }

    public int getIndex() {
        return index;
    }

    public ThreadState getThreadState() {
        return threadState;
    }

    public boolean isPriorityChangeable() {
        return priorityChangeable;
    }

    public String getCurrentData() {
        return currentData;
    }

    public String getType() {
        return type;
    }
}
