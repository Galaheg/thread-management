package com.hemre.senderthread.service;

import com.hemre.senderthread.thread.BaseThread;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SetThreadPriorityService {

    private final CommonListService commonListService;

    @Autowired
    public SetThreadPriorityService(CommonListService commonListService){
        this.commonListService = commonListService;
    }

    public String setThreadPriority(int index, int priority) {

        if (index >= 0 && index < commonListService.getThreads().size()) {

            BaseThread thread = commonListService.getThread(index);

            if (thread.isPriorityChangeable()){
                thread.setPriority(priority);
                return index + ". Sender thread's priority set to " + priority;
            }
            else
                return "Thread is not changeable";
        }

        return "Please enter a valid index";
    }

}
