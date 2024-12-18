package com.hemre.receiverthread.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.LinkedBlockingQueue;

@Service
public class KafkaQueueService {

    private final BlockingQueue<String> messageQueue = new LinkedBlockingQueue<>();
    private final List<SseEmitter> queueEmitters = new CopyOnWriteArrayList<>();

    @Autowired
    public KafkaQueueService(){

    }

    public void addMessage(String message){
        messageQueue.offer(message);
    }

    public List<String> getCurrentQueue(){
        return messageQueue.stream().toList();
    }

    public List<SseEmitter> getQueueEmitters(){
        return queueEmitters;
    }

    public void addToEmitterQueue(SseEmitter sseEmitter){
        queueEmitters.add(sseEmitter);
    }

    public void removeEmitter(SseEmitter sseEmitter){
        queueEmitters.remove(sseEmitter);
    }
}
