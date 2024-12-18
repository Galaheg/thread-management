package com.hemre.receiverthread.thread;

import com.hemre.receiverthread.dto.ThreadDTO;
import com.hemre.receiverthread.service.CommonListService;
import com.hemre.receiverthread.service.MinimizedThreadInfoService;
import com.hemre.receiverthread.service.ThreadUpdateSchedulerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

class ThreadUpdateSchedulerServiceTest {

    @Mock
    private CommonListService commonListService;

    @Mock
    private MinimizedThreadInfoService minimizedThreadInfoService;

    @Mock
    private ScheduledExecutorService scheduler;

    @InjectMocks
    private ThreadUpdateSchedulerService threadUpdateSchedulerService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void startThreadUpdateScheduler_ShouldScheduleThreadUpdateTask() {

        List<ThreadDTO> threadInfos = Collections.singletonList(mock(ThreadDTO.class));
        when(minimizedThreadInfoService.getThreadInfos()).thenReturn(threadInfos);

        threadUpdateSchedulerService.startThreadUpdateScheduler();

        // scheduler'ın task'ı belirli bir periyotla schedule ettiğini doğrula
        verify(scheduler).scheduleAtFixedRate(any(Runnable.class), eq(0L), eq(2L), eq(TimeUnit.SECONDS));
    }

    @Test
    void streamThreadUpdates_ShouldReturnEmitterAndAddToEmitters() {

        SseEmitter returnedEmitter = threadUpdateSchedulerService.streamThreadUpdates();

        assertNotNull(returnedEmitter);
        verify(commonListService).addEmitter(returnedEmitter);
    }


}
