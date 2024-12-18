package com.hemre.senderthread.service;

import com.hemre.senderthread.enums.ThreadStateEnum;
import com.hemre.senderthread.thread.BaseThread;
import com.hemre.senderthread.thread.SenderThread;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RestartThreadService {

    private final CommonListService commonListService;
    private final SenderThreadAddService senderThreadAddService;

    @Autowired
    public RestartThreadService(CommonListService commonListService, SenderThreadAddService senderThreadAddService) {
        this.commonListService = commonListService;
        this.senderThreadAddService = senderThreadAddService;
    }

    public String restartThread(int index) {
        if (index >= 0 && index < commonListService.getThreads().size()) {
            BaseThread thread = commonListService.getThread(index);
            if (thread.getThreadState().equals(ThreadStateEnum.STOPPED)) {
                if (commonListService.getThread(index) instanceof SenderThread) {
                    BaseThread oldThread = stoppedThread(index);
                    SenderThread sender = senderThreadAddService.newSenderThread(index, oldThread.isPriorityChangeable());
                    sender.start();
                    commonListService.setSenderThreadIndex(commonListService.getSenderThreads().indexOf(oldThread), sender);
                    commonListService.setBaseThreadIndex(index, sender);
                    return (index + 1) + ". Sender Thread restarted";
                }
            } else {
                return "Sender Thread needs to be at Stopped state to be restarted";
            }
        }

        return "Invalid index";
    }

    public void restartAllThreads() {
        for (BaseThread t : commonListService.getThreads()) {
            if (t.getThreadState().equals(ThreadStateEnum.STOPPED)) {
                restartThread(t.getIndex());
            }
        }
    }

    // This is for restarting threads returns stopped thread
    public BaseThread stoppedThread(int index) {
        BaseThread thread = commonListService.getThread(index);
        thread.stopThread();
        return thread;
    }

}
