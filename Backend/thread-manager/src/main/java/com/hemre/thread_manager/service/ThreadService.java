package com.hemre.thread_manager.service;

import com.hemre.thread_manager.component.QueueManager;
import com.hemre.thread_manager.dto.ThreadDTO;
import com.hemre.thread_manager.thread.BaseThread;
import com.hemre.thread_manager.thread.ReceiverThread;
import com.hemre.thread_manager.thread.SenderThread;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;

@Service
public class ThreadService {

    private final BlockingQueue<String> queue;
    private final List<SenderThread> senders = new ArrayList<>();
    private final List<ReceiverThread> receivers = new ArrayList<>();
    private final List<BaseThread> threads = new ArrayList<>();
    private int index = 0;

    public ThreadService(QueueManager queueManager) {
        this.queue = queueManager.getQueue();
    }

    public void createSenders(int count, boolean priorityChangeable) {
        for (int i = 0; i < count; i++) {
            SenderThread sender = new SenderThread(queue, index++, priorityChangeable);
            threads.add(sender);
            senders.add(sender);
        }
    }

    public void createReceivers(int count, boolean priorityChangeable) {
        for (int i = 0; i < count; i++) {
            ReceiverThread receiver = new ReceiverThread(queue, index++, priorityChangeable);
            receivers.add(receiver);
            threads.add(receiver);
        }
    }

    public void startAllThreads(){
        for(Thread t : threads){
            t.start();
        }
    }

    public void stopAllThreads() {
        for (Thread t : threads) {
            if (t instanceof SenderThread) {
                ((SenderThread) t).stopThread();
            } else if (t instanceof ReceiverThread) {
                ((ReceiverThread) t).stopThread();
            }
        }
    }

    public void startThread(int index) {
        Thread thread = threads.get(index);
        thread.start();
    }

    public BaseThread stopThread(int index) {
        BaseThread thread = threads.get(index);
        thread.stopThread();
        return thread;
    }

    public void restartThread(int index) {

        if(threads.get(index) instanceof SenderThread){
            if (index >= 0 && index < senders.size()) {
                BaseThread oldThread = stopThread(index);
                SenderThread sender = new SenderThread(queue, index, oldThread.isPriorityChangeable());
                sender.start();
                senders.set(senders.indexOf(oldThread), sender);
                threads.set(index, sender);
            }
        }
        else{
            if (index >= 0 && index < receivers.size()) {
                BaseThread oldThread = (ReceiverThread) stopThread(index);
                ReceiverThread receiver = new ReceiverThread(queue, index, oldThread.isPriorityChangeable());
                receiver.start();
                receivers.set(receivers.indexOf(oldThread), receiver);
                threads.set(index, receiver);
            }
        }
    }

    public String setThreadPriority(int index, int priority) {

        if (index >= 0 && index < threads.size()) {

            BaseThread thread = threads.get(index);

                if (thread.isPriorityChangeable()){
                    thread.setPriority(priority);
                    return index + ". thread's priority set to " + priority;
                }
                else
                    return "Thread is not changeable";
            }

        return "Please enter a valid index";
    }

    public List<SenderThread> getSenders() {
        return senders;
    }

    public List<ThreadDTO> getThreadInfos() {
        List<ThreadDTO> threadStates = new ArrayList<>();
        ThreadDTO threadDTO;
        for (BaseThread t : threads) {

            threadDTO = new ThreadDTO(
                    t.getIndex(),
                    t.getCurrentData(),
                    t.getThreadState(),
                    t.isPriorityChangeable(),
                    t.getType(),
                    t.getPriority()
            );

            threadStates.add(threadDTO);
        }
        return threadStates;
    }

}
