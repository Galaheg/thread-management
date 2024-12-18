package com.hemre.senderthread.thread;

import com.hemre.senderthread.enums.ThreadStateEnum;
import com.hemre.senderthread.service.CommonListService;
import com.hemre.senderthread.service.RestartThreadService;
import com.hemre.senderthread.service.SenderThreadAddService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class RestartThreadServiceTest {

    private CommonListService commonListService;
    private RestartThreadService restartThreadService;
    private SenderThreadAddService senderThreadAddService;
    private KafkaTemplate<String, String> kafkaTemplate;

    @BeforeEach
    void setUp() {
        // Mock CommonListService
        commonListService = mock(CommonListService.class);
        senderThreadAddService = mock(SenderThreadAddService.class);
        kafkaTemplate = mock(KafkaTemplate.class);
        restartThreadService = new RestartThreadService(commonListService, senderThreadAddService);
    }

    @Test
    void restartThread_ShouldRestartStoppedThread() {

        int index = 0;
        SenderThread stoppedThread = mock(SenderThread.class);
        SenderThread newThread = new SenderThread(null, index, stoppedThread.isPriorityChangeable(), kafkaTemplate);

        when(stoppedThread.getThreadState()).thenReturn(ThreadStateEnum.STOPPED);
        when(commonListService.getThread(index)).thenReturn(stoppedThread);
        when(commonListService.getThreads()).thenReturn(List.of(stoppedThread));
        when(senderThreadAddService.newSenderThread(index, stoppedThread.isPriorityChangeable())).thenReturn(newThread);

        String result = restartThreadService.restartThread(index);

        assertEquals("1. Sender Thread restarted", result);
    }

    @Test
    void restartThread_ShouldReturnError_WhenIndexIsInvalid() {

        int invalidIndex = -1;

        when(commonListService.getThreads()).thenReturn(List.of());

        String result = restartThreadService.restartThread(invalidIndex);

        assertEquals("Invalid index", result);
    }

    @Test
    void restartThread_ShouldReturnError_WhenThreadIsNotStopped() {

        int index = 0;
        when(commonListService.getThreads()).thenReturn(List.of(mock(SenderThread.class)));
        SenderThread runningThread = mock(SenderThread.class);
        when(runningThread.getThreadState()).thenReturn(ThreadStateEnum.RUNNING);
        when(commonListService.getThread(index)).thenReturn(runningThread);

        String result = restartThreadService.restartThread(index);

        assertEquals("Sender Thread needs to be at Stopped state to be restarted", result);
    }
}
