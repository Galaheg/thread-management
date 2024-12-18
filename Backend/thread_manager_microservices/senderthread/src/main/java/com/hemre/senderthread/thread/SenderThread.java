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
        if (threadStateEnum.equals(ThreadStateEnum.STOPPED)) {
            logger.info(index + ". Sender thread is restarting");
        }
        threadStateEnum = ThreadStateEnum.RUNNING;
        logger.info(index + ". Sender thread is Running");
        while (runable) {
            try {
                currentData = "Data-" + System.nanoTime(); // Random message for testing
                kafkaTemplate.send("threading2", currentData);  // Send data to Kafka Queue
                logger.info((index + 1) + " . Sender added: " + currentData +
                        " works at " + Integer.toString(this.getPriority()) + ". priority");
                Thread.sleep(1000); // frequency -> 1sn
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

}
