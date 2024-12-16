package com.hemre.thread_manager.controller;

import com.hemre.thread_manager.component.QueueManager;
import com.hemre.thread_manager.dto.ThreadDTO;
import com.hemre.thread_manager.service.ThreadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/api/threads")
@CrossOrigin(origins = "http://localhost:5173")
public class ThreadController {

    private final ThreadService threadService;
    private final QueueManager queueManager;
    private final List<SseEmitter> emitters = new CopyOnWriteArrayList<>();
    private final List<SseEmitter> queueEmitters = new CopyOnWriteArrayList<>();
    private final ScheduledExecutorService scheduler;

    @Autowired
    public ThreadController(ThreadService threadService, QueueManager queueManager, ScheduledExecutorService scheduledExecutorService) {
        this.threadService = threadService;
        this.queueManager = queueManager;
        this.scheduler = scheduledExecutorService;

        startThreadUpdateScheduler();
        startQueueUpdateScheduler();
    }


    @PostMapping("/add-senders")
    public String createSenders(@RequestParam int count, @RequestParam boolean isPriorityChangeable) {
        threadService.createSenders(count, isPriorityChangeable);
        return count + " sender threads created.";
    }

    @PostMapping("/add-receivers")
    public String createReceivers(@RequestParam int count, @RequestParam boolean isPriorityChangeable) {
        threadService.createReceivers(count, isPriorityChangeable);
        return count + " receiver threads created.";
    }

    @PostMapping("/start-thread")
    public String startThread(@RequestParam int index) {
        threadService.startThread(index);
        return index + ". thread started.";
    }

    @PostMapping("/stop-thread")
    public String stopThread(@RequestParam int index) {
        threadService.stopThread(index);
        return "Thread " + index + " stopped.";
    }

    @PostMapping("/start-all-threads")
    public String startAllThreads() {
        threadService.startAllThreads();
        return "All Threads started.";
    }

    @PostMapping("/stop-all-threads")
    public String stopAllThreads() {
        threadService.stopAllThreads();
        return "All Threads stopped.";
    }

    @PostMapping("/change-thread-priority")
    public String setThreadPriority(@RequestParam int index, @RequestParam int priority) {
        String message = threadService.setThreadPriority(index, priority);
        return message;
    }

    @PostMapping("/restart-thread")
    public String restartSenderThread(@RequestParam int index) {
        threadService.restartThread(index);
        return index + ". thread restarted";
    }

    @GetMapping("/queue")
    public Object getQueueState() {
        return queueManager.getQueue();
    }

    @GetMapping("/get-thread-infos")
    public List<ThreadDTO> getSenderThreads() {
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


    @GetMapping(value = "/queue-stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter streamQueueUpdates() {
        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);
        queueEmitters.add(emitter);

        emitter.onCompletion(() -> queueEmitters.remove(emitter));
        emitter.onTimeout(() -> queueEmitters.remove(emitter));

        return emitter;
    }
    
    public void startQueueUpdateScheduler() {
        scheduler.scheduleAtFixedRate(() -> {
            Object queueState = queueManager.getQueue(); // Kuyruk durumunu al
            queueEmitters.forEach(emitter -> {
                try {
                    emitter.send(SseEmitter.event().name("queue-update").data(queueState));
                } catch (Exception e) {
                    queueEmitters.remove(emitter); // Hata durumunda emitter'ı kaldır
                }
            });
        }, 0, 2, TimeUnit.SECONDS); // Her 2 saniyede bir gönderim
    }

}
