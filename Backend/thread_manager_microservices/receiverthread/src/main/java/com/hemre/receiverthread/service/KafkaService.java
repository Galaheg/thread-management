package com.hemre.receiverthread.service;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.*;

@Service
public class KafkaService {

    private final KafkaConsumer<String, String> consumer;

    public KafkaService() {
        Properties props = new Properties();
        props.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.setProperty(ConsumerConfig.GROUP_ID_CONFIG, "kafka-monitor-group");
        props.setProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        this.consumer = new KafkaConsumer<>(props);
        consumer.subscribe(Collections.singletonList("threading2"));
    }

    public Map<String, Object> getQueueStatus() {
        Map<String, Object> status = new HashMap<>();

        try {
            ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(1000));
            List<Map<String, Object>> messages = new ArrayList<>();

            for (ConsumerRecord<String, String> record : records) {
                Map<String, Object> message = new HashMap<>();
                message.put("key", record.key());
                message.put("value", record.value());
                message.put("partition", record.partition());
                message.put("offset", record.offset());
                messages.add(message);
            }

            status.put("topic", "threading2");
            status.put("partition", 0);
            status.put("currentOffset", getLatestOffset());
            status.put("messages", messages);

        } catch (Exception e) {
            status.put("error", "Error fetching Kafka queue status: " + e.getMessage());
        }
        return status;
    }

    private long getLatestOffset() {
        // En son offset bilgisini dönmek için bir dummy değer
        return System.currentTimeMillis(); // Bunu değiştirebilirsiniz
    }
}
