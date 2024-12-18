package com.hemre.receiverthread.thread;

import com.hemre.receiverthread.service.CommonListService;
import com.hemre.receiverthread.service.ReceiverThreadAddService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class ReceiverThreadAddServiceTest {

    private CommonListService commonListService;
    private ReceiverThreadAddService receiverThreadAddService;

    @BeforeEach
    void setUp() {
        commonListService = mock(CommonListService.class);
        receiverThreadAddService = new ReceiverThreadAddService(commonListService);
    }

    @Test
    void addReceiverThread_ShouldAddCorrectNumberOfThreads() {

        int count = 3;
        boolean priorityChangeable = true;

        receiverThreadAddService.addReceiverThread(count, priorityChangeable);

        ArgumentCaptor<ReceiverThread> captor = ArgumentCaptor.forClass(ReceiverThread.class);
        verify(commonListService, times(count)).addThread(captor.capture());

        List<ReceiverThread> capturedThreads = captor.getAllValues();

        assertEquals(count, capturedThreads.size());

        for (int i = 0; i < count; i++) {
            ReceiverThread thread = capturedThreads.get(i);
            assertEquals(i, thread.getIndex());
            assertEquals(priorityChangeable, thread.isPriorityChangeable());
        }
    }

    @Test
    void addReceiverThread_ShouldHandleZeroCount() {

        int count = 0;

        receiverThreadAddService.addReceiverThread(count, true);

        verify(commonListService, never()).addThread(any(ReceiverThread.class));
    }
}
