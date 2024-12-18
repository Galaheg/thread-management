package com.hemre.receiverthread.thread;

import com.hemre.receiverthread.enums.ThreadStateEnum;
import com.hemre.receiverthread.service.CommonListService;
import com.hemre.receiverthread.service.RestartThreadService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class RestartThreadServiceTest {

    private CommonListService commonListService;
    private RestartThreadService restartThreadService;

    @BeforeEach
    void setUp() {

        commonListService = mock(CommonListService.class);
        restartThreadService = new RestartThreadService(commonListService);
    }

    @Test
    void restartThread_ShouldRestartStoppedThread() {

        int index = 0;
        ReceiverThread stoppedThread = mock(ReceiverThread.class);
        when(stoppedThread.getThreadState()).thenReturn(ThreadStateEnum.STOPPED);
        when(commonListService.getThread(index)).thenReturn(stoppedThread);
        when(commonListService.getThreads()).thenReturn(List.of(stoppedThread));

        ReceiverThread newThread = new ReceiverThread(index, stoppedThread.isPriorityChangeable(), stoppedThread.getLatch());
        newThread.setLastOffset(stoppedThread.getLastOffset());

        String result = restartThreadService.restartThread(index);

        assertEquals("1. Receiver Thread restarted", result);

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
        when(commonListService.getThreads()).thenReturn(List.of(mock(ReceiverThread.class)));
        ReceiverThread runningThread = mock(ReceiverThread.class);
        when(runningThread.getThreadState()).thenReturn(ThreadStateEnum.RUNNING);
        when(commonListService.getThread(index)).thenReturn(runningThread);

        String result = restartThreadService.restartThread(index);

        assertEquals("Receiver Thread needs to be at Stopped state to be restarted", result);
    }
}
