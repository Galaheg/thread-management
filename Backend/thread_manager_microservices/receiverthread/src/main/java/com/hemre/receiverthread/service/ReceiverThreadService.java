package com.hemre.receiverthread.service;

import com.hemre.receiverthread.dto.ThreadDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;

// Main Service
@Service
public class ReceiverThreadService {

    private final ReceiverThreadAddService receiverThreadAddService;
    private final StartThreadService startThreadService;
    private final StopThreadService stopThreadService;
    private final RestartThreadService restartThreadService;
    private final SetThreadPriorityService setThreadPriorityService;
    private final MinimizedThreadInfoService minimizedThreadInfoService;
    private final ThreadUpdateSchedulerService threadUpdateSchedulerService;

    @Autowired
    public ReceiverThreadService(ReceiverThreadAddService receiverThreadAddService, StartThreadService startThreadService,
                                 StopThreadService stopThreadService, RestartThreadService restartThreadService,
                                 SetThreadPriorityService setThreadPriorityService, MinimizedThreadInfoService minimizedThreadInfoService,
                                 ThreadUpdateSchedulerService threadUpdateSchedulerService) {

        this.receiverThreadAddService = receiverThreadAddService;
        this.startThreadService = startThreadService;
        this.stopThreadService = stopThreadService;
        this.restartThreadService = restartThreadService;
        this.setThreadPriorityService = setThreadPriorityService;
        this.minimizedThreadInfoService = minimizedThreadInfoService;
        this.threadUpdateSchedulerService = threadUpdateSchedulerService;

        startThreadUpdateScheduler();
    }

    public void startThreadUpdateScheduler() {
        threadUpdateSchedulerService.startThreadUpdateScheduler();
    }

    public SseEmitter streamThreadUpdates() {
        return threadUpdateSchedulerService.streamThreadUpdates();
    }

    public void createReceivers(int count, boolean priorityChangeable) {
        receiverThreadAddService.addReceiverThread(count, priorityChangeable);
    }

    // Start Thread
    public String startThread(int index) {
        return startThreadService.startThread(index);
    }

    public void startAllThreads() {
        startThreadService.startAllThreads();
    }

    public String stopThread(int index) {
        return stopThreadService.stopThread(index);
    }

    public void stopAllThreads() {
        stopThreadService.stopAllThreads();
    }

    public String restartThread(int index) {
        return restartThreadService.restartThread(index);
    }

    public void restartAllThreads() {
        restartThreadService.restartAllThreads();
        ;
    }

    public String setThreadPriority(int index, int priority) {
        return setThreadPriorityService.setThreadPriority(index, priority);
    }

    public List<ThreadDTO> getThreadInfos() {
        return minimizedThreadInfoService.getThreadInfos();
    }


}
