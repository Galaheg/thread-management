package com.hemre.receiverthread.service;

import com.hemre.receiverthread.enums.ThreadStateEnum;
import com.hemre.receiverthread.thread.BaseThread;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StartThreadService {

    private final CommonListService commonListService;

    @Autowired
    public StartThreadService(CommonListService commonListService) {
        this.commonListService = commonListService;
    }

    public String startThread(int index) {
        if (index >= 0 && index < commonListService.getReceiverThreads().size()) {
            BaseThread thread = commonListService.getThread(index);
            if (thread.getThreadState().equals(ThreadStateEnum.WAITING)) {
                thread.start();
                return (index + 1) + ". Receiver Thread started";
            } else if (thread.getThreadState().equals(ThreadStateEnum.RUNNING)) {
                return "Thread already Running";
            } else if (thread.getThreadState().equals(ThreadStateEnum.STOPPED)) {
                return "Stopped thread cannot be started directly. Need to restart";
            }
        }
        return "Invalid index";
    }


    public void startAllThreads() {
        for (BaseThread t : commonListService.getThreads()) {
            if (t.getThreadState().equals(ThreadStateEnum.WAITING)) {
                t.start();
            }
        }
    }

}
