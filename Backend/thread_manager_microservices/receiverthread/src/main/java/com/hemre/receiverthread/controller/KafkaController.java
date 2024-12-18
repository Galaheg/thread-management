package com.hemre.receiverthread.controller;

import com.hemre.receiverthread.service.KafkaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;
import java.util.concurrent.ScheduledExecutorService;

@RestController
@RequestMapping("/api/kafka")
@CrossOrigin(origins = "http://localhost:5173")
public class KafkaController {


    private KafkaService kafkaService;

    @Autowired
    public KafkaController(KafkaService kafkaService, ScheduledExecutorService scheduledExecutorService) {
        this.kafkaService = kafkaService;
    }

    @GetMapping("/queue-status")
    public ResponseEntity<List<String>> getQueueStatus() {
        List<String> messages = kafkaService.getCurrentQueue();
        return ResponseEntity.ok(messages);
    }

    @GetMapping(value = "/queue-stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter streamQueueUpdates() {
        return kafkaService.streamQueueUpdates();
    }


}
