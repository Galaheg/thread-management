package com.hemre.receiverthread.controller;

import com.hemre.receiverthread.dto.ThreadDTO;
import com.hemre.receiverthread.service.ReceiverThreadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/api/receivers")
@CrossOrigin(origins = "http://localhost:5173")
public class ReceiverController {

    private final ReceiverThreadService threadService;
    private final List<SseEmitter> emitters = new CopyOnWriteArrayList<>();
    private final ScheduledExecutorService scheduler;

    @Autowired
    public ReceiverController(ReceiverThreadService threadService, ScheduledExecutorService scheduledExecutorService) {
        this.threadService = threadService;
        this.scheduler = scheduledExecutorService;
        startThreadUpdateScheduler();
    }

    @PostMapping("/add-receivers")
    public String createReceivers(@RequestParam int count, @RequestParam boolean isPriorityChangeable) {
        threadService.createReceivers(count, isPriorityChangeable);
        return count + " receiver threads created.";
    }

    @PostMapping("/start-thread")
    public String startThread(@RequestParam int index) {
        String message = threadService.startThread(index);
        return message;
    }

    @PostMapping("/start-all-threads")
    public String startAllWaitingThreads() {
        threadService.startAllThreads();
        return "All Waiting Threads started.";
    }

    @PostMapping("/stop-thread")
    public String stopThread(@RequestParam int index) {
        String message = threadService.stopThread(index);
        return message;
    }

    @PostMapping("/stop-all-threads")
    public String stopAllRunningThreads() {
        threadService.stopAllThreads();
        return "All Running Threads stopped.";
    }

    @PostMapping("/restart-thread")
    public String restartSenderThread(@RequestParam int index) {
        String message = threadService.restartThread(index);
        return message;
    }

    @PostMapping("/restart-all-threads")
    public String restartAllStoppedThreads() {
        threadService.restartAllThreads();
        return  "All Stopped threads restarted";
    }

    @PostMapping("/change-thread-priority")
    public String setThreadPriority(@RequestParam int index, @RequestParam int priority) {
        String message = threadService.setThreadPriority(index, priority);
        return message;
    }

    @GetMapping("/get-thread-infos")
    public List<ThreadDTO> getThreadDTOs() {
        return threadService.getThreadInfos();
    }

    @GetMapping(value = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter streamThreadUpdates() {
        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);
        emitters.add(emitter);

        emitter.onCompletion(() -> emitters.remove(emitter));
        emitter.onTimeout(() -> emitters.remove(emitter));

        return emitter;
    }

    // Arka planda her 1 saniyede bir thread bilgilerini emit eder.
    public void startThreadUpdateScheduler() {
        scheduler.scheduleAtFixedRate(() -> {
            List<ThreadDTO> threadInfos = threadService.getThreadInfos(); // Mevcut thread durumlarını al
            emitters.forEach(emitter -> {
                try {
                    emitter.send(SseEmitter.event().name("threads-update").data(threadInfos));
                } catch (Exception e) {
                    emitters.remove(emitter); // Eğer bağlantı başarısızsa emitter'ı kaldır
                }
            });
        }, 0, 2, TimeUnit.SECONDS); // 2 saniyede bir gönderim
    }
}