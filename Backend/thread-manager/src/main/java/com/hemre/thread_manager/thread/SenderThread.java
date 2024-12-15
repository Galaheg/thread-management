package com.hemre.thread_manager.thread;

import com.hemre.thread_manager.enums.ThreadStateEnum;
import com.hemre.thread_manager.enums.ThreadTypeEnum;

import java.util.concurrent.BlockingQueue;

public class SenderThread extends BaseThread {


    public SenderThread(BlockingQueue<String> queue, int index, boolean isPriorityChangeable) {
        super(queue, index, isPriorityChangeable, ThreadTypeEnum.SENDER);
    }

    @Override
    public void run() {
        threadStateEnum =  ThreadStateEnum.RUNNING;
        while (runable) {
            try {
                currentData = "Data-" + System.currentTimeMillis();
                queue.put(currentData);
                System.out.println(index + " . Sender added: " + currentData +
                        " works at " + Integer.toString(this.getPriority()) + ". priority");
                Thread.sleep(1000); // frequency -> 1sn
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

}
