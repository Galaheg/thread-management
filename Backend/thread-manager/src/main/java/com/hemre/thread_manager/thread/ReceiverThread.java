package com.hemre.thread_manager.thread;

import com.hemre.thread_manager.enums.ThreadStateEnum;
import com.hemre.thread_manager.enums.ThreadTypeEnum;

import java.util.concurrent.BlockingQueue;

public class ReceiverThread extends BaseThread {


    public ReceiverThread(BlockingQueue<String> queue, int index, boolean isPriorityChangeable) {
        super(queue, index, isPriorityChangeable, ThreadTypeEnum.RECEIVER);
    }

    @Override
    public void run() {
        threadStateEnum = ThreadStateEnum.RUNNING;

        while (runable) {
            try {
                currentData = queue.take();
                System.out.println(index + ". Receiver processed: " + currentData +
                        " works at " + Integer.toString(this.getPriority()) + ". priority");
                Thread.sleep(1000); // frequency -> 1sn
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }


}
