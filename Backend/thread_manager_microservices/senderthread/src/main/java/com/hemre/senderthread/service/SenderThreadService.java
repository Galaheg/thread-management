package com.hemre.senderthread.service;

import com.hemre.senderthread.dto.ThreadDTO;
import com.hemre.senderthread.enums.ThreadStateEnum;
import com.hemre.senderthread.thread.BaseThread;
import com.hemre.senderthread.thread.SenderThread;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service
public class SenderThreadService {

    private final CommonListService commonListService;
    private final SenderThreadAddService senderThreadAddService;
    private final StartThreadService startThreadService;
    private final StopThreadService stopThreadService;
    private final RestartThreadService restartThreadService;
    private final SetThreadPriorityService setThreadPriorityService;
    private final MinimizedThreadInfoService minimizedThreadInfoService;
    private final ThreadUpdateSchedulerService threadUpdateSchedulerService;

    private final ScheduledExecutorService scheduler;
    private int index = 0;

    @Autowired
    public SenderThreadService(ScheduledExecutorService scheduledExecutorService,
                               CommonListService commonListService, SenderThreadAddService senderThreadAddService,
                               StartThreadService startThreadService, StopThreadService stopThreadService,
                               RestartThreadService restartThreadService, SetThreadPriorityService setThreadPriorityService,
                               MinimizedThreadInfoService minimizedThreadInfoService, ThreadUpdateSchedulerService threadUpdateSchedulerService) {

        this.scheduler = scheduledExecutorService;
        this.commonListService = commonListService;
        this.senderThreadAddService = senderThreadAddService;
        this.startThreadService = startThreadService;
        this.stopThreadService = stopThreadService;
        this.restartThreadService = restartThreadService;
        this.setThreadPriorityService = setThreadPriorityService;
        this.minimizedThreadInfoService = minimizedThreadInfoService;
        this.threadUpdateSchedulerService = threadUpdateSchedulerService;

        startThreadUpdateScheduler();
    }

    public void startThreadUpdateScheduler(){
        threadUpdateSchedulerService.startThreadUpdateScheduler();
    }

    public SseEmitter streamThreadUpdates(){
        return threadUpdateSchedulerService.streamThreadUpdates();
    }

    public void createSenders(int count, boolean priorityChangeable) {
        senderThreadAddService.addSenderThread(count, priorityChangeable);
    }

    public String startThread(int index) {
        return startThreadService.startThread(index);
    }

    public void startAllThreads(){
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
