package com.hemre.receiverthread.service;

import com.hemre.receiverthread.thread.BaseThread;
import com.hemre.receiverthread.thread.ReceiverThread;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
public class CommonListService {

    // Common lists that services are using
    private final List<ReceiverThread> receivers = new ArrayList<>();
    private final List<BaseThread> threads = new ArrayList<>();
    private final List<SseEmitter> emitters = new CopyOnWriteArrayList<>();

    public void addThread(BaseThread thread) {
        threads.add(thread);
        if (thread instanceof ReceiverThread) {
            receivers.add((ReceiverThread) thread);
        }
    }

    public BaseThread getThread(int index) {
        return threads.get(index);
    }

    public ReceiverThread getReceiverThread(int index) {
        return receivers.get(index);
    }

    public List<BaseThread> getThreads() {
        return threads;
    }

    public List<ReceiverThread> getReceiverThreads() {
        return receivers;
    }

    public void setReceiverThreadIndex(int index, ReceiverThread receiverThread) {
        receivers.set(index, receiverThread);
    }

    public void setBaseThreadIndex(int index, ReceiverThread receiverThread) {
        threads.set(index, receiverThread);
    }

    // Add emitter
    public void addEmitter(SseEmitter emitter) {
        emitters.add(emitter);
    }

    //Remove emitter
    public void removeEmitter(SseEmitter emitter) {
        emitters.remove(emitter);
    }

    public List<SseEmitter> getEmitters() {
        return emitters;
    }

}
