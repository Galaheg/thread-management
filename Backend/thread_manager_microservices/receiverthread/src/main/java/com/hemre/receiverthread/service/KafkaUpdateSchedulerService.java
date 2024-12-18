package com.hemre.receiverthread.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service
public class KafkaUpdateSchedulerService {

    private final ScheduledExecutorService scheduler;
    private final KafkaQueueService kafkaQueueService;

    @Autowired
    public KafkaUpdateSchedulerService(ScheduledExecutorService scheduledExecutorService, KafkaQueueService kafkaQueueService){

        this.scheduler = scheduledExecutorService;
        this.kafkaQueueService = kafkaQueueService;

    }

    public SseEmitter streamQueueUpdates(){
        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);
        kafkaQueueService.addToEmitterQueue(emitter);

        emitter.onCompletion(() -> kafkaQueueService.removeEmitter(emitter));
        emitter.onTimeout(() -> kafkaQueueService.removeEmitter(emitter));

        return emitter;
    }

    public void startQueueUpdateScheduler(){
        scheduler.scheduleAtFixedRate(() -> {
            List<String> messages = kafkaQueueService.getCurrentQueue(); // Kafka'dan mesajları al
            kafkaQueueService.getQueueEmitters().forEach(emitter -> {
                try {
                    emitter.send(SseEmitter.event().name("queue-update").data(messages));
                } catch (Exception e) {
                    kafkaQueueService.removeEmitter(emitter); // Hata durumunda emitter'ı kaldır
                }
            });
        }, 0, 2, TimeUnit.SECONDS); // Her 2 saniyede bir gönderim
    }

}
