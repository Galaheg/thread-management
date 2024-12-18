package com.hemre.receiverthread.thread;

import com.hemre.receiverthread.enums.ThreadStateEnum;
import com.hemre.receiverthread.service.CommonListService;
import com.hemre.receiverthread.service.StopThreadService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class StopThreadServiceTest {

    private CommonListService commonListService;
    private StopThreadService stopThreadService;

    @BeforeEach
    void setUp() {
        commonListService = mock(CommonListService.class);
        stopThreadService = new StopThreadService(commonListService);
    }

    @Test
    void stopThread_ShouldReturnError_WhenThreadIsWaiting() {

        int index = 0;
        ReceiverThread waitingThread = mock(ReceiverThread.class);
        when(waitingThread.getThreadState()).thenReturn(ThreadStateEnum.WAITING);
        when(commonListService.getThread(index)).thenReturn(waitingThread);
        when(commonListService.getThreads()).thenReturn(List.of(waitingThread));

        String result = stopThreadService.stopThread(index);

        assertEquals("Thread was not Running", result);
        verify(waitingThread, never()).stopThread();
        verify(waitingThread, never()).shutDown();
    }

    @Test
    void stopThread_ShouldStopRunningThread() {

        int index = 0;
        ReceiverThread runningThread = mock(ReceiverThread.class);
        when(runningThread.getThreadState()).thenReturn(ThreadStateEnum.RUNNING);
        when(commonListService.getThread(index)).thenReturn(runningThread);
        when(commonListService.getThreads()).thenReturn(List.of(runningThread));

        String result = stopThreadService.stopThread(index);

        assertEquals("Thread stopped", result);
        verify(runningThread).stopThread();
        verify(runningThread).shutDown();
    }

    @Test
    void stopThread_ShouldReturnError_WhenThreadIsStopped() {

        int index = 0;
        ReceiverThread stoppedThread = mock(ReceiverThread.class);
        when(stoppedThread.getThreadState()).thenReturn(ThreadStateEnum.STOPPED);
        when(commonListService.getThread(index)).thenReturn(stoppedThread);
        when(commonListService.getThreads()).thenReturn(List.of(stoppedThread));

        String result = stopThreadService.stopThread(index);

        assertEquals("Thread already stopped", result);
        verify(stoppedThread, never()).stopThread();
        verify(stoppedThread, never()).shutDown();
    }

    @Test
    void stopThread_ShouldReturnError_WhenIndexIsInvalid() {

        int invalidIndex = -1;
        when(commonListService.getThreads()).thenReturn(List.of());

        String result = stopThreadService.stopThread(invalidIndex);

        assertEquals("Invalid index", result);
    }

    @Test
    void stopAllThreads_ShouldStopAllRunningThreads() {

        ReceiverThread runningThread1 = mock(ReceiverThread.class);
        ReceiverThread runningThread2 = mock(ReceiverThread.class);
        when(runningThread1.getThreadState()).thenReturn(ThreadStateEnum.RUNNING);
        when(runningThread2.getThreadState()).thenReturn(ThreadStateEnum.RUNNING);
        when(commonListService.getThreads()).thenReturn(List.of(runningThread1, runningThread2));

        stopThreadService.stopAllThreads();

        verify(runningThread1).stopThread();
        verify(runningThread1).shutDown();
        verify(runningThread2).stopThread();
        verify(runningThread2).shutDown();
    }

    @Test
    void stopAllThreads_ShouldNotStopNonRunningThreads() {

        ReceiverThread waitingThread = mock(ReceiverThread.class);
        ReceiverThread stoppedThread = mock(ReceiverThread.class);
        when(waitingThread.getThreadState()).thenReturn(ThreadStateEnum.WAITING);
        when(stoppedThread.getThreadState()).thenReturn(ThreadStateEnum.STOPPED);
        when(commonListService.getThreads()).thenReturn(List.of(waitingThread, stoppedThread));

        stopThreadService.stopAllThreads();

        verify(waitingThread, never()).stopThread();
        verify(waitingThread, never()).shutDown();
        verify(stoppedThread, never()).stopThread();
        verify(stoppedThread, never()).shutDown();
    }
}
