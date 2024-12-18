package com.hemre.receiverthread.service;

import com.hemre.receiverthread.thread.ReceiverThread;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.CountDownLatch;

@Service
public class ReceiverThreadAddService {

    private final CommonListService commonListService;
    private int index = 0;

    @Autowired
    public ReceiverThreadAddService(CommonListService commonListService) {
        this.commonListService = commonListService;
    }

    public void addReceiverThread(int count, boolean priorityChangeable) {
        for (int i = 0; i < count; i++) {
            ReceiverThread receiver = new ReceiverThread(index++, priorityChangeable, new CountDownLatch(1));
            commonListService.addThread(receiver);
        }
    }

}
