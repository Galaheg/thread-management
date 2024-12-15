package com.hemre.thread_manager.service;

import com.hemre.thread_manager.component.QueueManager;
import com.hemre.thread_manager.dto.ThreadDTO;
import com.hemre.thread_manager.threads.ReceiverThread;
import com.hemre.thread_manager.threads.SenderThread;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;

@Service
public class ThreadService {

    private final BlockingQueue<String> queue;
    private final List<SenderThread> senders = new ArrayList<>();
    private final List<ReceiverThread> receivers = new ArrayList<>();
    private final List<Thread> threads = new ArrayList<>();
    private int index = 0;
    private int receiverIndex = 0;

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

    public Thread stopThread(int index) {
        Thread thread = threads.get(index);
        if (thread instanceof SenderThread) {
            ((SenderThread) thread).stopThread();
        } else if (thread instanceof ReceiverThread) {
            ((ReceiverThread) thread).stopThread();
        }
        return thread;
    }

    public void restartSender(int index) {
        if (index >= 0 && index < senders.size()) {
            SenderThread oldThread = (SenderThread) stopThread(index);
            SenderThread sender = new SenderThread(queue, index, oldThread.isPriorityChangeable());
            sender.start();
            senders.set(index, sender);
        }
    }

    public void restartReceiver(int index) {
        if (index >= 0 && index < senders.size()) {
            ReceiverThread oldThread = (ReceiverThread) stopThread(index);
            ReceiverThread receiver = new ReceiverThread(queue, index, oldThread.isPriorityChangeable());
            receiver.start();
            receivers.set(index, receiver);
        }
    }

    public void setSenderPriority(int index, int priority) {
        if (index >= 0 && index < senders.size()) {
            senders.get(index).setPriority(priority);
        }
    }

    public void setReceiverPriority(int index, int priority) {
        if (index >= 0 && index < receivers.size()) {
            receivers.get(index).setPriority(priority);
        }
    }

    public List<SenderThread> getSenders() {
        return senders;
    }

    public List<ThreadDTO> getThreadInfos() {
        List<ThreadDTO> threadStates = new ArrayList<>();
        ThreadDTO threadDTO;
        for (Thread t : threads) {


            // Create DTO for each thread
            if(t instanceof SenderThread){
                 threadDTO = new ThreadDTO(
                        ((SenderThread) t).getIndex(),
                        ((SenderThread) t).getCurrentData(),
                        ((SenderThread) t).getThreadState(),
                        ((SenderThread) t).isPriorityChangeable(),
                        ((SenderThread) t).getType()
                );
            }
            else{
                 threadDTO = new ThreadDTO(
                        ((ReceiverThread) t).getIndex(),
                        ((ReceiverThread) t).getCurrentData(),
                        ((ReceiverThread) t).getThreadState(),
                        ((ReceiverThread) t).isPriorityChangeable(),
                        ((ReceiverThread) t).getType()
                );
            }
            threadStates.add(threadDTO);
        }
        return threadStates;
    }

}
