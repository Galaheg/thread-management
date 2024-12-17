package com.hemre.receiverthread.controller;

import com.hemre.receiverthread.service.KafkaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/api/kafka")
@CrossOrigin(origins = "http://localhost:5173")
public class KafkaController {

    private final List<SseEmitter> queueEmitters = new CopyOnWriteArrayList<>();
    private final ScheduledExecutorService scheduler;
    private KafkaService kafkaService;

    @Autowired
    public KafkaController(KafkaService kafkaService, ScheduledExecutorService scheduledExecutorService){
        this.kafkaService = kafkaService;
        this.scheduler = scheduledExecutorService;

        startQueueUpdateScheduler();
    }


//    @GetMapping("/queue-status")
//    public ResponseEntity<Map<String, Object>> getQueueStatus() {
//        Map<String, Object> status = kafkaService.getQueueStatus();
//        return ResponseEntity.ok(status);
//    }
//
//    @GetMapping("/queue-status")
//    public List<String> getQueueStatus() {
//        List<String> status = kafkaService.getQueueStatus();
//        return status;
//    }

    @GetMapping("/queue-status")
    public ResponseEntity<List<String>> getQueueStatus() {
        List<String> messages = kafkaService.getCurrentQueue();
        return ResponseEntity.ok(messages);
    }

    @GetMapping(value = "/queue-stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter streamQueueUpdates() {
        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);
        queueEmitters.add(emitter);

        emitter.onCompletion(() -> queueEmitters.remove(emitter));
        emitter.onTimeout(() -> queueEmitters.remove(emitter));

        return emitter;
    }

    private void startQueueUpdateScheduler() {
        scheduler.scheduleAtFixedRate(() -> {
            List<String> messages = kafkaService.getCurrentQueue(); // Kafka'dan mesajları al
            queueEmitters.forEach(emitter -> {
                try {
                    emitter.send(SseEmitter.event().name("queue-update").data(messages));
                } catch (Exception e) {
                    queueEmitters.remove(emitter); // Hata durumunda emitter'ı kaldır
                }
            });
        }, 0, 2, TimeUnit.SECONDS); // Her 2 saniyede bir gönderim
    }

}
