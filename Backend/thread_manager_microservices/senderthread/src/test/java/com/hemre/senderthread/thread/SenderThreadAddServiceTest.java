package com.hemre.senderthread.thread;

import com.hemre.senderthread.service.CommonListService;
import com.hemre.senderthread.service.SenderThreadAddService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

class SenderThreadAddServiceTest {

    private CommonListService commonListService;
    private KafkaTemplate<String, String> kafkaTemplate;
    private SenderThreadAddService senderThreadAddService;

    @BeforeEach
    void setUp() {
        commonListService = mock(CommonListService.class);
        kafkaTemplate = mock(KafkaTemplate.class);
        senderThreadAddService = new SenderThreadAddService(commonListService, kafkaTemplate);
    }

    @Test
    void addReceiverThread_ShouldAddCorrectNumberOfThreads() {
        int count = 3;
        boolean priorityChangeable = true;

        senderThreadAddService.addSenderThread(count, priorityChangeable);

        ArgumentCaptor<SenderThread> captor = ArgumentCaptor.forClass(SenderThread.class);
        verify(commonListService, times(count)).addThread(captor.capture());

        List<SenderThread> capturedThreads = captor.getAllValues();

        assertEquals(count, capturedThreads.size());

        for (int i = 0; i < count; i++) {
            SenderThread thread = capturedThreads.get(i);
            assertEquals(i, thread.getIndex());
            assertEquals(priorityChangeable, thread.isPriorityChangeable());
        }
    }

    @Test
    void addReceiverThread_ShouldHandleZeroCount() {
        int count = 0;

        senderThreadAddService.addSenderThread(count, true);

        verify(commonListService, never()).addThread(any(SenderThread.class));
    }

    @Test
    void newSenderThread_ShouldCreateNewSenderThread() {
        int index = 5;
        boolean priorityChangeable = true;

        SenderThread senderThread = senderThreadAddService.newSenderThread(index, priorityChangeable);

        assertNotNull(senderThread);
        assertEquals(index, senderThread.getIndex());
        assertEquals(priorityChangeable, senderThread.isPriorityChangeable());

    }
}
