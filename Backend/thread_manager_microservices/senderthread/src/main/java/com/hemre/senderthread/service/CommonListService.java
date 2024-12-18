package com.hemre.senderthread.service;

import com.hemre.senderthread.thread.BaseThread;
import com.hemre.senderthread.thread.SenderThread;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
public class CommonListService {

    private final List<SenderThread> senders = new ArrayList<>();
    private final List<BaseThread> threads = new ArrayList<>();
    private final List<SseEmitter> emitters = new CopyOnWriteArrayList<>();

    public void addThread(BaseThread thread) {
        threads.add(thread);
        if (thread instanceof SenderThread) {
            senders.add((SenderThread) thread);
        }
    }

    public BaseThread getThread(int index) {
        return threads.get(index);
    }

    // Receiver thread listesine thread alma
    public SenderThread getSenderThread(int index) {
        return senders.get(index);
    }

    // Thread'leri alma
    public List<BaseThread> getThreads() {
        return threads;
    }
    public List<SenderThread> getSenderThreads() {
        return senders;
    }

    public void setSenderThreadIndex(int index, SenderThread senderThread){
        senders.set(index, senderThread);
    }

    public void setBaseThreadIndex(int index, SenderThread senderThread){
        threads.set(index, senderThread);
    }

    // Emitters listesine emitter ekleme
    public void addEmitter(SseEmitter emitter) {
        emitters.add(emitter);
    }

    // Emitters listesine emitter silme
    public void removeEmitter(SseEmitter emitter) {
        emitters.remove(emitter);
    }

    // Emitters listesine tüm emitters alma
    public List<SseEmitter> getEmitters() {
        return emitters;
    }

}
