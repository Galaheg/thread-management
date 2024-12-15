package com.hemre.thread_manager.thread;

import com.hemre.thread_manager.enums.ThreadStateEnum;
import com.hemre.thread_manager.enums.ThreadTypeEnum;

import java.util.concurrent.BlockingQueue;

public class BaseThread extends Thread{

    protected final BlockingQueue<String> queue;
    protected volatile boolean runable = true;
    protected final int index;
    protected String currentData;
    protected boolean priorityChangeable;
    protected final ThreadTypeEnum type;
    protected ThreadStateEnum threadStateEnum = ThreadStateEnum.WAITING;

    public BaseThread(BlockingQueue<String> queue, int index, boolean isPriorityChangeable, ThreadTypeEnum type){
        this.queue = queue;
        this.index = index;
        this.priorityChangeable = isPriorityChangeable;
        this.type= type;
    }

    public BaseThread stopThread() {
        threadStateEnum = ThreadStateEnum.STOPPED;
        runable = false;
        return this;
    }

    public ThreadTypeEnum getType() {
        return type;
    }

    public boolean isPriorityChangeable() {
        return priorityChangeable;
    }

    public ThreadStateEnum getThreadState() {
        return threadStateEnum;
    }

    public String getCurrentData() {
        return currentData;
    }

    public int getIndex() {
        return index;
    }
}
