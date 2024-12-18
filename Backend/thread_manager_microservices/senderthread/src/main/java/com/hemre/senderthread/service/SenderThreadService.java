package com.hemre.senderthread.service;

import com.hemre.senderthread.dto.ThreadDTO;
import com.hemre.senderthread.enums.ThreadStateEnum;
import com.hemre.senderthread.thread.BaseThread;
import com.hemre.senderthread.thread.SenderThread;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service
public class SenderThreadService {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final List<SenderThread> senders = new ArrayList<>();
    private final List<BaseThread> threads = new ArrayList<>();
    private final List<SseEmitter> emitters = new CopyOnWriteArrayList<>();
    private final ScheduledExecutorService scheduler;
    private int index = 0;

    @Autowired
    public SenderThreadService(KafkaTemplate<String, String> kafkaTemplate, ScheduledExecutorService scheduledExecutorService) {
        this.kafkaTemplate = kafkaTemplate;
        this.scheduler = scheduledExecutorService;
        startThreadUpdateScheduler();
    }

    public void startThreadUpdateScheduler(){
        scheduler.scheduleAtFixedRate(() -> {
            List<ThreadDTO> threadInfos = getThreadInfos(); // Mevcut thread durumlarını al
            emitters.forEach(emitter -> {
                try {
                    emitter.send(SseEmitter.event().name("sender-threads-update").data(threadInfos));
                } catch (Exception e) {
                    emitters.remove(emitter); // Eğer bağlantı başarısızsa emitter'ı kaldır
                }
            });
        }, 0, 2, TimeUnit.SECONDS); // 2 saniyede bir gönderim
    }

    public SseEmitter streamThreadUpdates(){
        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);
        emitters.add(emitter);

        emitter.onCompletion(() -> emitters.remove(emitter));
        emitter.onTimeout(() -> emitters.remove(emitter));

        return emitter;
    }

    public void createSenders(int count, boolean priorityChangeable) {
        for (int i = 0; i < count; i++) {
            SenderThread sender = new SenderThread(null, index++, priorityChangeable, kafkaTemplate);
            senders.add(sender);
            threads.add(sender);
        }
    }

    public String startThread(int index) {
        BaseThread thread = threads.get(index);
        if(thread.getThreadState().equals(ThreadStateEnum.WAITING)){
            thread.start();
            return (index+1) + ". Sender Thread started";
        }
        else if(thread.getThreadState().equals(ThreadStateEnum.RUNNING)){
            return "Thread already Running";
        }
        else if (thread.getThreadState().equals(ThreadStateEnum.STOPPED)) {
            return "Stopped thread cannot be started directly. Need to restart";
        }
        return "Invalid index";
    }

    public String stopThread(int index) {
        BaseThread thread = threads.get(index);

        if(thread.getThreadState().equals(ThreadStateEnum.WAITING)){
            return "Thread was not Running";
        }
        else if(thread.getThreadState().equals(ThreadStateEnum.RUNNING)){
            thread.stopThread();
            return "Thread stopped";
        }
        else if (thread.getThreadState().equals(ThreadStateEnum.STOPPED)) {
            return "Thread already stopped";
        }

        return "Invalid index";
    }

    public void startAllThreads(){
        for(BaseThread t : threads){
            if(t.getThreadState().equals(ThreadStateEnum.WAITING)){
                t.start();
            }
        }
    }

    public BaseThread stoppedThread(int index) {
        BaseThread thread = threads.get(index);
        thread.stopThread();
        return thread;
    }

    public void stopAllThreads() {
        for (BaseThread t : threads) {
            if (t.getThreadState().equals(ThreadStateEnum.RUNNING)) {
                t.stopThread();
            }
        }
    }

    public String restartThread(int index) {
        BaseThread thread = threads.get(index);
        if (thread.getThreadState().equals(ThreadStateEnum.STOPPED)) {
            if (threads.get(index) instanceof SenderThread) {
                if (index >= 0 && index < threads.size()) {
                    BaseThread oldThread = stoppedThread(index);
                    SenderThread sender = new SenderThread(null, index, oldThread.isPriorityChangeable(), kafkaTemplate);
                    sender.start();
                    senders.set(senders.indexOf(oldThread), sender);
                    threads.set(index, sender);
                    return (index+1) +". Sender Thread restarted";
                }
            }
        }
        else{
            return "Sender thread needs to be at Stopped state to be restarted";
        }
        return "Invalid index";
    }

    public void restartAllThreads() {
        for (BaseThread t : threads) {
            if (t.getThreadState().equals(ThreadStateEnum.STOPPED)) {
                restartThread(t.getIndex());
            }
        }
    }

    public String setThreadPriority(int index, int priority) {

        if (index >= 0 && index < threads.size()) {

            BaseThread thread = threads.get(index);

            if (thread.isPriorityChangeable()){
                thread.setPriority(priority);
                return index + ". Sender thread's priority set to " + priority;
            }
            else
                return "Thread is not changeable";
        }

        return "Please enter a valid index";
    }

    public List<ThreadDTO> getThreadInfos() {
        List<ThreadDTO> threadStates = new ArrayList<>();
        ThreadDTO threadDTO;
        for (BaseThread t : threads) {

            threadDTO = new ThreadDTO(
                    t.getIndex(),
                    t.getCurrentData(),
                    t.getThreadState(),
                    t.isPriorityChangeable(),
                    t.getType(),
                    t.getPriority()
            );

            threadStates.add(threadDTO);
        }
        return threadStates;
    }

}
