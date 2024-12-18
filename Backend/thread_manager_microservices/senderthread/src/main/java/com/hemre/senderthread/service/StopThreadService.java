package com.hemre.senderthread.service;

import com.hemre.senderthread.enums.ThreadStateEnum;
import com.hemre.senderthread.thread.BaseThread;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StopThreadService {

    private final CommonListService commonListService;

    @Autowired
    public StopThreadService(CommonListService commonListService) {
        this.commonListService = commonListService;
    }

    public String stopThread(int index) {
        if (index >= 0 && index < commonListService.getThreads().size()) {
            BaseThread thread = commonListService.getThread(index);

            if (thread.getThreadState().equals(ThreadStateEnum.WAITING)) {
                return "Thread was not Running";
            } else if (thread.getThreadState().equals(ThreadStateEnum.RUNNING)) {
                thread.stopThread();
                return "Thread stopped";
            } else if (thread.getThreadState().equals(ThreadStateEnum.STOPPED)) {
                return "Thread already stopped";
            }


        }
        return "Invalid index";
    }

    public void stopAllThreads() {
        for (BaseThread t : commonListService.getThreads()) {
            if (t.getThreadState().equals(ThreadStateEnum.RUNNING)) {
                t.stopThread();
            }
        }
    }
}
