package com.hemre.senderthread.thread;

import com.hemre.senderthread.enums.ThreadStateEnum;
import com.hemre.senderthread.enums.ThreadTypeEnum;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.concurrent.BlockingQueue;

public class SenderThread extends BaseThread {


    private final KafkaTemplate<String, String> kafkaTemplate;

    public SenderThread(BlockingQueue<String> queue, int index, boolean isPriorityChangeable
    , KafkaTemplate<String, String> kafkaTemplate) {
        super(queue, index, isPriorityChangeable, ThreadTypeEnum.SENDER);
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public void run() {
        threadStateEnum =  ThreadStateEnum.RUNNING;
        while (runable) {
            try {
                currentData = "Data-" + System.currentTimeMillis();
                kafkaTemplate.send("threading2", currentData);  // Kafka topic'ine veri gönder
                System.out.println((index+1) + " . Sender added: " + currentData +
                        " works at " + Integer.toString(this.getPriority()) + ". priority");
                Thread.sleep(1000); // frequency -> 1sn
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

}
