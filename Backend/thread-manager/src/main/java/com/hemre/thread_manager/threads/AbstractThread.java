package com.hemre.thread_manager.threads;

public abstract class AbstractThread extends Thread{

    public abstract AbstractThread stopThread();
    public abstract String getType();
    public abstract boolean isPriorityChangeable();
    public abstract ThreadState getThreadState();
    public abstract String getCurrentData();
    public abstract int getIndex();


}
