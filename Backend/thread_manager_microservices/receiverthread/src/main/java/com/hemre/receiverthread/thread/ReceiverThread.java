package com.hemre.receiverthread.thread;

import com.hemre.receiverthread.config.KafkaConfig;
import com.hemre.receiverthread.enums.ThreadStateEnum;
import com.hemre.receiverthread.enums.ThreadTypeEnum;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.errors.WakeupException;

import java.time.Duration;
import java.util.Arrays;
import java.util.concurrent.CountDownLatch;

public class ReceiverThread extends BaseThread {

    private KafkaConsumer<String, String> consumer;
    private CountDownLatch latch;
    private long lastOffset = 0;
    private final KafkaConfig kafkaConfig = new KafkaConfig();

    public ReceiverThread(int index, boolean isPriorityChangeable,
                          CountDownLatch countDownLatch) {
        super(null, index, isPriorityChangeable, ThreadTypeEnum.RECEIVER);
        this.latch = countDownLatch;
        this.consumer = new KafkaConsumer<String, String>(kafkaConfig.consumerConfigs());
        consumer.subscribe(Arrays.asList("threading2")); // subscribe to our topic
    }

    @Override
    public void run() {
        if (threadStateEnum.equals(ThreadStateEnum.STOPPED))
            logger.info(index + ". Receiver thread is restarting");

        threadStateEnum = ThreadStateEnum.RUNNING;
        logger.info(index + ". Receiver thread is Running");

        try {
            consumer.poll(Duration.ofMillis(1000));

            // Partition offset assignment for situations like restarting a thread
            for (org.apache.kafka.common.TopicPartition partition : consumer.assignment()) {
                System.out.println("Seeking to offset: " + lastOffset + " for partition: " + partition.partition());
                consumer.seek(partition, lastOffset);
            }
            while (runable) {

                ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(1000));

                for (ConsumerRecord record : records) {
                    logger.info((index + 1) + ". receiver Thread read " +
                            "Key: " + record.key() +
                            " Record value --> " + record.value() +
                            " Partition: " + record.partition() +
                            " Offset: " + record.offset()
                    );
                    currentData = record.value().toString();
                    consumer.commitSync();  // Senkron commit

                    lastOffset = record.offset() + 1;
                    Thread.sleep(1000); // 1sn freq
                    if (!runable)
                        break;
                }

            }
        } catch (WakeupException e) {
            System.out.println(e.getMessage());
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            consumer.close();
            latch.countDown();
        }

    }

    public void shutDown() {
        consumer.wakeup(); // to stop consumer
    }

    public KafkaConsumer<String, String> getConsumer() {
        return consumer;
    }

    public void setConsumer(KafkaConsumer<String, String> consumer) {
        this.consumer = consumer;
    }

    public CountDownLatch getLatch() {
        return latch;
    }

    public void setLatch(CountDownLatch latch) {
        this.latch = latch;
    }

    public long getLastOffset() {
        return lastOffset;
    }

    public void setLastOffset(long lastOffset) {
        this.lastOffset = lastOffset;
    }
}
