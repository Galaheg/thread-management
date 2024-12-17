package com.hemre.senderthread.dto;

import com.hemre.senderthread.enums.ThreadStateEnum;
import com.hemre.senderthread.enums.ThreadTypeEnum;

public class ThreadDTO {

    private int index;
    private String currentData;
    private ThreadStateEnum threadStateEnum;
    private boolean priorityChangeable;
    private final ThreadTypeEnum type;
    private int priority;


    public ThreadDTO(int index, String currentData,
                     ThreadStateEnum state, boolean priorityChangeable, ThreadTypeEnum type, int priority) {
        this.index = index;
        this.currentData = currentData;
        this.threadStateEnum = state;
        this.priorityChangeable = priorityChangeable;
        this.type = type;
        this.priority = priority;
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

    public ThreadStateEnum getThreadState() {
        return threadStateEnum;
    }

    public void setThreadState(ThreadStateEnum threadStateEnum) {
        this.threadStateEnum = threadStateEnum;
    }

    public boolean isPriorityChangeable() {
        return priorityChangeable;
    }

    public ThreadTypeEnum getType() {
        return type;
    }

    public int getPriority() {
        return priority;
    }
}
