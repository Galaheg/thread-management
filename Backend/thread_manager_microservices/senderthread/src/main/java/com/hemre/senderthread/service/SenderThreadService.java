package com.hemre.senderthread.service;

import com.hemre.senderthread.dto.ThreadDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;

// Main service
@Service
public class SenderThreadService {

    private final SenderThreadAddService senderThreadAddService;
    private final StartThreadService startThreadService;
    private final StopThreadService stopThreadService;
    private final RestartThreadService restartThreadService;
    private final SetThreadPriorityService setThreadPriorityService;
    private final MinimizedThreadInfoService minimizedThreadInfoService;
    private final ThreadUpdateSchedulerService threadUpdateSchedulerService;

    @Autowired
    public SenderThreadService(SenderThreadAddService senderThreadAddService,
                               StartThreadService startThreadService, StopThreadService stopThreadService,
                               RestartThreadService restartThreadService, SetThreadPriorityService setThreadPriorityService,
                               MinimizedThreadInfoService minimizedThreadInfoService, ThreadUpdateSchedulerService threadUpdateSchedulerService) {

        this.senderThreadAddService = senderThreadAddService;
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

    public void createSenders(int count, boolean priorityChangeable) {
        senderThreadAddService.addSenderThread(count, priorityChangeable);
    }

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
    }

    public String setThreadPriority(int index, int priority) {
        return setThreadPriorityService.setThreadPriority(index, priority);
    }

    public List<ThreadDTO> getThreadInfos() {
        return minimizedThreadInfoService.getThreadInfos();
    }

}
