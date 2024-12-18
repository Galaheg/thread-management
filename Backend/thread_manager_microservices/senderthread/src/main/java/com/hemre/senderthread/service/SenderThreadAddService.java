package com.hemre.senderthread.service;

import com.hemre.senderthread.thread.SenderThread;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.CountDownLatch;

@Service
public class SenderThreadAddService {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final CommonListService commonListService;
    private int index = 0;

    @Autowired
    public SenderThreadAddService(CommonListService commonListService, KafkaTemplate<String, String> kafkaTemplate){
        this.commonListService = commonListService;
        this.kafkaTemplate = kafkaTemplate;
    }

    public void addSenderThread(int count, boolean priorityChangeable){
        for (int i = 0; i < count; i++) {
            SenderThread sender = new SenderThread(null, index++, priorityChangeable, kafkaTemplate);
            commonListService.addThread(sender);
        }
    }

    public SenderThread newSenderThread(int index, boolean priorityChangeable){
        SenderThread senderThread = new SenderThread(null, index, priorityChangeable,kafkaTemplate);
        return senderThread;
    }
}
