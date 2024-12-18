package com.hemre.senderthread.service;

import com.hemre.senderthread.dto.ThreadDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service
public class ThreadUpdateSchedulerService {

    private final CommonListService commonListService;
    private final ScheduledExecutorService scheduler;
    private final  MinimizedThreadInfoService minimizedThreadInfoService;

    @Autowired
    public ThreadUpdateSchedulerService(CommonListService commonListService, ScheduledExecutorService scheduledExecutorService, MinimizedThreadInfoService minimizedThreadInfoService){
        this.commonListService = commonListService;
        this.minimizedThreadInfoService = minimizedThreadInfoService;
        this.scheduler = scheduledExecutorService;
    }

    public void startThreadUpdateScheduler(){
        scheduler.scheduleAtFixedRate(() -> {
            List<ThreadDTO> threadInfos = minimizedThreadInfoService.getThreadInfos(); // Mevcut thread durumlarını al
            commonListService.getEmitters().forEach(emitter -> {
                try {
                    emitter.send(SseEmitter.event().name("sender-threads-update").data(threadInfos));
                } catch (Exception e) {
                    commonListService.removeEmitter(emitter); // Eğer bağlantı başarısızsa emitter'ı kaldır
                }
            });
        }, 0, 2, TimeUnit.SECONDS); // 2 saniyede bir gönderim
    }

    public SseEmitter streamThreadUpdates(){
        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);
        commonListService.addEmitter(emitter);

        emitter.onCompletion(() -> commonListService.removeEmitter(emitter));
        emitter.onTimeout(() -> commonListService.removeEmitter(emitter));

        return emitter;
    }

}
