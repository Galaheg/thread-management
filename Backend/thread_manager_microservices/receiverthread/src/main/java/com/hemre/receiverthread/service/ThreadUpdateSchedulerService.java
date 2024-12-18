package com.hemre.receiverthread.service;

import com.hemre.receiverthread.dto.ThreadDTO;
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
    private final MinimizedThreadInfoService minimizedThreadInfoService;

    @Autowired
    public ThreadUpdateSchedulerService(ScheduledExecutorService scheduledExecutorService, CommonListService commonListService,
                                        MinimizedThreadInfoService minimizedThreadInfoService){
        this.commonListService = commonListService;
        this.scheduler = scheduledExecutorService;
        this.minimizedThreadInfoService = minimizedThreadInfoService;
    }

    public void startThreadUpdateScheduler(){
        // Emit Thread information every 2 seconds;
        scheduler.scheduleAtFixedRate(() -> {
            List<ThreadDTO> threadInfos = minimizedThreadInfoService.getThreadInfos();
            commonListService.getEmitters().forEach(emitter -> {
                try {
                    emitter.send(SseEmitter.event().name("receiver-threads-update").data(threadInfos));
                } catch (Exception e) {
                    commonListService.removeEmitter(emitter); // Bağlantı başarısızsa emitter'ı kaldır
                }
            });
        }, 0, 2, TimeUnit.SECONDS);
    }

    public SseEmitter streamThreadUpdates() {
        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);
        commonListService.addEmitter(emitter);

        emitter.onCompletion(() -> commonListService.removeEmitter(emitter));
        emitter.onTimeout(() -> commonListService.removeEmitter(emitter));

        return emitter;
    }


}
