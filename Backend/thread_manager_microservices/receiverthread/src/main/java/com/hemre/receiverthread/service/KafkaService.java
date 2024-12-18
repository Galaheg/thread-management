package com.hemre.receiverthread.service;

import com.hemre.receiverthread.component.GroupIdProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;
import java.util.concurrent.ScheduledExecutorService;

@Service
public class KafkaService {

    private GroupIdProvider groupIdProvider;

    private final KafkaQueueService kafkaQueueService;
    private final KafkaUpdateSchedulerService kafkaUpdateSchedulerService;
    private final ScheduledExecutorService scheduler;

    private String groupID;

    @Autowired
    public KafkaService(ScheduledExecutorService scheduledExecutorService, GroupIdProvider groupIdProvider,
                        KafkaQueueService kafkaQueueService, KafkaUpdateSchedulerService kafkaUpdateSchedulerService) {
        // Group ID'nin her başlatmada farklı olması sağlanmalı
        this.groupID = "queue-lister-group" + System.currentTimeMillis();
        this.scheduler = scheduledExecutorService;
        this.groupIdProvider = groupIdProvider;
        this.kafkaQueueService = kafkaQueueService;
        this.kafkaUpdateSchedulerService = kafkaUpdateSchedulerService;
        startQueueUpdateScheduler();
    }

    @KafkaListener(topics = "threading2", groupId = "#{@groupIdProvider.provideName()}")
    // used SpEL for dynamic groupId
    public void consume(String message) {
        kafkaQueueService.addMessage(message);
    }

    public SseEmitter streamQueueUpdates() {
        return kafkaUpdateSchedulerService.streamQueueUpdates();
    }

    public void startQueueUpdateScheduler() {
        kafkaUpdateSchedulerService.startQueueUpdateScheduler();// Her 2 saniyede bir gönderim
    }

    public List<String> getCurrentQueue() {
        return kafkaQueueService.getCurrentQueue().stream().toList();
    }

    public String getGroupID() {
        return groupID;
    }
}
