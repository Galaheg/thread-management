package com.hemre.receiverthread.service;

import com.hemre.receiverthread.component.GroupIdProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;
import java.util.concurrent.*;

@Service
public class KafkaService {

    private GroupIdProvider groupIdProvider;
    private final BlockingQueue<String> messageQueue = new LinkedBlockingQueue<>();
    private final List<SseEmitter> queueEmitters = new CopyOnWriteArrayList<>();
    private final ScheduledExecutorService scheduler;

    private String groupID;

    @Autowired
    public KafkaService(ScheduledExecutorService scheduledExecutorService, GroupIdProvider groupIdProvider) {
        // Group ID'nin her başlatmada farklı olmasını sağlamak
        this.groupID = "queue-lister-group" + System.currentTimeMillis();
        this.scheduler = scheduledExecutorService;
        this.groupIdProvider = groupIdProvider;
        startQueueUpdateScheduler();
    }

    public SseEmitter streamQueueUpdates(){
        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);
        queueEmitters.add(emitter);

        emitter.onCompletion(() -> queueEmitters.remove(emitter));
        emitter.onTimeout(() -> queueEmitters.remove(emitter));

        return emitter;
    }

    public void startQueueUpdateScheduler(){
        scheduler.scheduleAtFixedRate(() -> {
            List<String> messages = getCurrentQueue(); // Kafka'dan mesajları al
            queueEmitters.forEach(emitter -> {
                try {
                    emitter.send(SseEmitter.event().name("queue-update").data(messages));
                } catch (Exception e) {
                    queueEmitters.remove(emitter); // Hata durumunda emitter'ı kaldır
                }
            });
        }, 0, 2, TimeUnit.SECONDS); // Her 2 saniyede bir gönderim
    }

    @KafkaListener(topics = "threading2", groupId = "#{@groupIdProvider.provideName()}")
    public void consume(String message) {
        messageQueue.offer(message); // add message to queue
    }

    public List<String> getCurrentQueue() {
        return messageQueue.stream().toList();
    }

    public String getGroupID() {
        return groupID;
    }
}
