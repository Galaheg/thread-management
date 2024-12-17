package com.hemre.receiverthread.service;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

@Service
public class KafkaService {

    private final BlockingQueue<String> messageQueue = new LinkedBlockingQueue<>();

    @KafkaListener(topics = "threading2", groupId = "queue-consumer-group")
    public void consume(String message) {
        messageQueue.offer(message); // add message to queue
    }

    public List<String> getCurrentQueue() {
        return messageQueue.stream().toList();
    }
}
