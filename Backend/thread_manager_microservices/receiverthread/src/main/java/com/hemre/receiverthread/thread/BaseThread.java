package com.hemre.receiverthread.thread;

import com.hemre.receiverthread.enums.ThreadStateEnum;
import com.hemre.receiverthread.enums.ThreadTypeEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.BlockingQueue;

public class BaseThread extends Thread{

    protected final BlockingQueue<String> queue;
    protected volatile boolean runable = true; // volatile faster and much safer for thread management
    protected final int index;
    protected String currentData;
    protected boolean priorityChangeable;
    protected final ThreadTypeEnum type;
    protected ThreadStateEnum threadStateEnum = ThreadStateEnum.WAITING;

    protected static final Logger logger = LoggerFactory.getLogger(BaseThread.class);


    public BaseThread(BlockingQueue<String> queue, int index, boolean isPriorityChangeable, ThreadTypeEnum type){
        this.queue = queue;
        this.index = index;
        this.priorityChangeable = isPriorityChangeable;
        this.type= type;
    }

    public BaseThread stopThread() {
        threadStateEnum = ThreadStateEnum.STOPPED;
        logger.info(index + ". Receiver thread has stopped");
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
