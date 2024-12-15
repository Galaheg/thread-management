package com.hemre.thread_manager.controller;

import com.hemre.thread_manager.component.QueueManager;
import com.hemre.thread_manager.dto.ThreadDTO;
import com.hemre.thread_manager.service.ThreadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/threads")@CrossOrigin(origins = "http://localhost:5173")

public class ThreadController {

    private final ThreadService ThreadService;
    private final QueueManager queueManager;

    @Autowired
    public ThreadController(ThreadService ThreadService2, QueueManager queueManager) {
        this.ThreadService = ThreadService2;
        this.queueManager = queueManager;
    }

    @PostMapping("/add-senders")
    public String createSenders(@RequestParam int count, @RequestParam boolean isPriorityChangeable) {
        ThreadService.createSenders(count, isPriorityChangeable);
        return count + " sender threads created.";
    }

    @PostMapping("/add-receivers")
    public String createReceivers(@RequestParam int count, @RequestParam boolean isPriorityChangeable) {
        ThreadService.createReceivers(count, isPriorityChangeable);
        return count + " receiver threads created.";
    }

    @PostMapping("/start-thread")
    public String startThread(@RequestParam int index) {
        ThreadService.startThread(index);
        return index + ". thread started.";
    }

    @PostMapping("/stop-thread")
    public String stopThread(@RequestParam int index) {
        ThreadService.stopThread(index);
        return "Thread " + index + " stopped.";
    }

    @PostMapping("/start-all-threads")
    public String startAllThreads() {
        ThreadService.startAllThreads();
        return "All Threads started.";
    }

    @PostMapping("/stop-all-threads")
    public String stopAllThreads() {
        ThreadService.stopAllThreads();
        return "All Threads stopped.";
    }

    @PostMapping("/sender-priority")
    public String setSenderThreadPriority(@RequestParam int index, @RequestParam int priority) {
        ThreadService.setSenderPriority(index, priority);
        return index + ". sender thread's priority set to " + priority;
    }

    @PostMapping("/receiver-priority")
    public String setReceiverThreadPriority(@RequestParam int index, @RequestParam int priority) {
        ThreadService.setReceiverPriority(index, priority);
        return index + ". receiver thread's priority set to " + priority;
    }

    @PostMapping("/restart-sender-thread")
    public String restartSenderThread(@RequestParam int index) {
        ThreadService.restartSender(index);
        return index + ". sender thread restarted";
    }

    @PostMapping("/restart-receiver-thread")
    public String restartReceiverThread(@RequestParam int index) {
        ThreadService.restartReceiver(index);
        return index + ". receiver thread restarted";
    }

    @GetMapping("/queue")
    public Object getQueueState() {
        return queueManager.getQueue();
    }

    @GetMapping("/get-thread-infos")
    public List<ThreadDTO> getSenderThreads() {
        return ThreadService.getThreadInfos();
    }

}
