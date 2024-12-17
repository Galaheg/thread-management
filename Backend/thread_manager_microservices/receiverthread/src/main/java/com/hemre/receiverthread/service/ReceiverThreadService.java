package com.hemre.receiverthread.service;

import com.hemre.receiverthread.dto.ThreadDTO;
import com.hemre.receiverthread.enums.ThreadStateEnum;
import com.hemre.receiverthread.thread.BaseThread;
import com.hemre.receiverthread.thread.ReceiverThread;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

@Service
public class ReceiverThreadService {

    private final List<ReceiverThread> receivers = new ArrayList<>();
    private final List<BaseThread> threads = new ArrayList<>();
    private int index = 0;

    public void createReceivers(int count, boolean priorityChangeable) {
        for (int i = 0; i < count; i++) {
            ReceiverThread receiver = new ReceiverThread(index++, priorityChangeable, new CountDownLatch(1));
            Thread thread = new Thread(receiver);
            threads.add(receiver);
            receivers.add(receiver);
        }
    }

    public String startThread(int index) {
        BaseThread thread = threads.get(index);
        if(thread.getThreadState().equals(ThreadStateEnum.WAITING)){
            thread.start();
            return (index+1) + ". Thread started";
        }
        else if(thread.getThreadState().equals(ThreadStateEnum.RUNNING)){
            return "Thread already Running";
        }
        else if (thread.getThreadState().equals(ThreadStateEnum.STOPPED)) {
            return "Stopped thread cannot be started directly. Need to restart";
        }
        return "Invalid index";
    }

    public void startAllThreads(){
        for(BaseThread t : threads){
            if(t.getThreadState().equals(ThreadStateEnum.WAITING)){
                t.start();
            }
        }
    }

    public BaseThread stoppedThread(int index) {
        BaseThread thread = threads.get(index);
        thread.stopThread();
        return thread;
    }

    public String stopThread(int index) {
        BaseThread thread = threads.get(index);

        if(thread.getThreadState().equals(ThreadStateEnum.WAITING)){
            return "Thread was not Running";
        }
        else if(thread.getThreadState().equals(ThreadStateEnum.RUNNING)){
            thread.stopThread();
            return "Thread stopped";
        }
        else if (thread.getThreadState().equals(ThreadStateEnum.STOPPED)) {
            return "Thread already stopped";
        }

        return "Invalid index";
    }

    public void stopAllThreads() {
        for (BaseThread t : threads) {
            if (t.getThreadState().equals(ThreadStateEnum.RUNNING)) {
                ((ReceiverThread)t).shutDown();
                t.stopThread();

            }
        }
    }

    public String restartThread(int index) {
        BaseThread thread = threads.get(index);
        if (thread.getThreadState().equals(ThreadStateEnum.STOPPED)) {
            if (threads.get(index) instanceof ReceiverThread) {
                if (index >= 0 && index < threads.size()) {
                    BaseThread oldThread = stoppedThread(index);
                    ReceiverThread receiver = new ReceiverThread(index, oldThread.isPriorityChangeable(), ((ReceiverThread)oldThread).getLatch());
                    receiver.setLastOffset(((ReceiverThread) oldThread).getLastOffset());
                    receiver.start();
                    receivers.set(receivers.indexOf(oldThread), receiver);
                    threads.set(index, receiver);
                    return (index+1) +". Receiver Thread restarted";
                }
            }
        }
        else{
            return "Receiver Thread needs to be at Stopped state to be restarted";
        }
        return "Invalid index";
    }

    public void restartAllThreads() {
        for (BaseThread t : threads) {
            if (t.getThreadState().equals(ThreadStateEnum.STOPPED)) {
                restartThread(t.getIndex());
            }
        }
    }

    public String setThreadPriority(int index, int priority) {

        if (index >= 0 && index < threads.size()) {

            BaseThread thread = threads.get(index);

            if (thread.isPriorityChangeable()){
                thread.setPriority(priority);
                return index + ". Receiver thread's priority set to " + priority;
            }
            else
                return "Thread is not changeable";
        }

        return "Please enter a valid index";
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
