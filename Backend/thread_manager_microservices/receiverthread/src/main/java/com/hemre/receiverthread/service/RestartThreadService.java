package com.hemre.receiverthread.service;

import com.hemre.receiverthread.enums.ThreadStateEnum;
import com.hemre.receiverthread.thread.BaseThread;
import com.hemre.receiverthread.thread.ReceiverThread;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RestartThreadService {

    private final CommonListService commonListService;

    @Autowired
    public RestartThreadService(CommonListService commonListService){
        this.commonListService = commonListService;
    }

    public String restartThread(int index){
        BaseThread thread = commonListService.getThread(index);
        if (thread.getThreadState().equals(ThreadStateEnum.STOPPED)) {
            if (commonListService.getThread(index) instanceof ReceiverThread) {
                if (index >= 0 && index < commonListService.getThreads().size()) {
                    BaseThread oldThread = stoppedThread(index);
                    ReceiverThread receiver = new ReceiverThread(index, oldThread.isPriorityChangeable(), ((ReceiverThread)oldThread).getLatch());
                    receiver.setLastOffset(((ReceiverThread) oldThread).getLastOffset());
                    receiver.start();
                    commonListService.setReceiverThreadIndex(commonListService.getReceiverThreads().indexOf(oldThread), receiver);
                    commonListService.setBaseThreadIndex(index, receiver);
                    return (index+1) +". Receiver Thread restarted";
                }
            }
        }
        else{
            return "Receiver Thread needs to be at Stopped state to be restarted";
        }
        return "Invalid index";
    }

    public void restartAllThreads(){
        for (BaseThread t : commonListService.getThreads()) {
            if (t.getThreadState().equals(ThreadStateEnum.STOPPED)) {
                restartThread(t.getIndex());
            }
        }
    }

    //This is for restarting thread. Returns stopped thread
    public BaseThread stoppedThread(int index) {
        BaseThread thread = commonListService.getThread(index);
        thread.stopThread();
        ((ReceiverThread)thread).shutDown();
        return thread;
    }
}
