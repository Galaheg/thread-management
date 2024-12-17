package com.hemre.receiverthread.controller;

import com.hemre.receiverthread.service.KafkaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/kafka")
@CrossOrigin(origins = "http://localhost:5173")
public class KafkaController {

    @Autowired
    private KafkaService kafkaService;

    @GetMapping("/queue-status")
    public ResponseEntity<Map<String, Object>> getQueueStatus() {
        Map<String, Object> status = kafkaService.getQueueStatus();
        return ResponseEntity.ok(status);
    }

}
