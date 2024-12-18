package com.hemre.senderthread.controller;

import com.hemre.senderthread.dto.ThreadDTO;
import com.hemre.senderthread.service.SenderThreadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/api/senders")
@CrossOrigin(origins = "http://localhost:5173")
public class SenderController {

    private final SenderThreadService threadService;
    private final List<SseEmitter> emitters = new CopyOnWriteArrayList<>();
    private final ScheduledExecutorService scheduler;

    @Autowired
    public SenderController(SenderThreadService threadService, ScheduledExecutorService scheduledExecutorService) {
        this.threadService = threadService;
        this.scheduler = scheduledExecutorService;
    }

    @PostMapping("/add-senders")
    public String createSenders(@RequestParam int count, @RequestParam boolean isPriorityChangeable) {
        threadService.createSenders(count, isPriorityChangeable);
        return count + " sender threads created.";
    }

    @PostMapping("/start-thread")
    public String startThread(@RequestParam int index) {
        String message = threadService.startThread(index);
        return message;
    }

    @PostMapping("/start-all-threads")
    public String startAllWaitingThreads() {
        threadService.startAllThreads();
        return "All Waiting Sender Threads started.";
    }

    @PostMapping("/stop-thread")
    public String stopThread(@RequestParam int index) {
        String message = threadService.stopThread(index);
        return message;
    }

    @PostMapping("/stop-all-threads")
    public String stopAllRunningThreads() {
        threadService.stopAllThreads();
        return "All Running Sender Threads stopped.";
    }

    @PostMapping("/restart-thread")
    public String restartSenderThread(@RequestParam int index) {
        String message = threadService.restartThread(index);
        return message;
    }

    @PostMapping("/restart-all-threads")
    public String restartAllStoppedThreads() {
        threadService.restartAllThreads();
        return  "All Stopped Sender threads restarted";
    }

    @PostMapping("/change-thread-priority")
    public String setThreadPriority(@RequestParam int index, @RequestParam int priority) {
        String message = threadService.setThreadPriority(index-1, priority);
        return message;
    }
    @GetMapping("/get-thread-infos")
    public List<ThreadDTO> getThreadDTOs() {
        return threadService.getThreadInfos();
    }

    @GetMapping(value = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter streamThreadUpdates() {
        return threadService.streamThreadUpdates();
    }

}
