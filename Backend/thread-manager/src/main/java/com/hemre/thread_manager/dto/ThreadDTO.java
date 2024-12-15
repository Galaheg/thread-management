package com.hemre.thread_manager.dto;

import com.hemre.thread_manager.threads.SenderThread;
import com.hemre.thread_manager.threads.ThreadState;

public class ThreadDTO {

    private int index;
    private String currentData;
    private ThreadState threadState;
    private boolean priorityChangeable;
    private final String type;


    public ThreadDTO(int index, String currentData,
                     ThreadState state, boolean priorityChangeable, String type) {
        this.index = index;
        this.currentData = currentData;
        this.threadState = state;
        this.priorityChangeable = priorityChangeable;
        this.type = type;
    }

    // Getters and setters
    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getCurrentData() {
        return currentData;
    }

    public void setCurrentData(String currentData) {
        this.currentData = currentData;
    }

    public ThreadState getThreadState() {
        return threadState;
    }

    public void setThreadState(ThreadState threadState) {
        this.threadState = threadState;
    }
}
