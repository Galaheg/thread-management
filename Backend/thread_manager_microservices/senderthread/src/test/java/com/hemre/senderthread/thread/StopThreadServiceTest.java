package com.hemre.senderthread.thread;

import com.hemre.senderthread.enums.ThreadStateEnum;
import com.hemre.senderthread.service.CommonListService;
import com.hemre.senderthread.service.StopThreadService;
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
        SenderThread waitingThread = mock(SenderThread.class);
        when(waitingThread.getThreadState()).thenReturn(ThreadStateEnum.WAITING);
        when(commonListService.getThread(index)).thenReturn(waitingThread);
        when(commonListService.getThreads()).thenReturn(List.of(waitingThread));

        String result = stopThreadService.stopThread(index);

        assertEquals("Thread was not Running", result);
        verify(waitingThread, never()).stopThread();
    }

    @Test
    void stopThread_ShouldStopRunningThread() {
        int index = 0;
        SenderThread runningThread = mock(SenderThread.class);
        when(runningThread.getThreadState()).thenReturn(ThreadStateEnum.RUNNING);
        when(commonListService.getThread(index)).thenReturn(runningThread);
        when(commonListService.getThreads()).thenReturn(List.of(runningThread));

        String result = stopThreadService.stopThread(index);

        assertEquals("Thread stopped", result);
        verify(runningThread).stopThread();
    }

    @Test
    void stopThread_ShouldReturnError_WhenThreadIsStopped() {

        int index = 0;
        SenderThread stoppedThread = mock(SenderThread.class);
        when(stoppedThread.getThreadState()).thenReturn(ThreadStateEnum.STOPPED);
        when(commonListService.getThread(index)).thenReturn(stoppedThread);
        when(commonListService.getThreads()).thenReturn(List.of(stoppedThread));

        String result = stopThreadService.stopThread(index);

        assertEquals("Thread already stopped", result);
        verify(stoppedThread, never()).stopThread();
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

        SenderThread runningThread1 = mock(SenderThread.class);
        SenderThread runningThread2 = mock(SenderThread.class);
        when(runningThread1.getThreadState()).thenReturn(ThreadStateEnum.RUNNING);
        when(runningThread2.getThreadState()).thenReturn(ThreadStateEnum.RUNNING);
        when(commonListService.getThreads()).thenReturn(List.of(runningThread1, runningThread2));

        stopThreadService.stopAllThreads();

        verify(runningThread1).stopThread();
        verify(runningThread2).stopThread();
    }

    @Test
    void stopAllThreads_ShouldNotStopNonRunningThreads() {

        SenderThread waitingThread = mock(SenderThread.class);
        SenderThread stoppedThread = mock(SenderThread.class);
        when(waitingThread.getThreadState()).thenReturn(ThreadStateEnum.WAITING);
        when(stoppedThread.getThreadState()).thenReturn(ThreadStateEnum.STOPPED);
        when(commonListService.getThreads()).thenReturn(List.of(waitingThread, stoppedThread));

        stopThreadService.stopAllThreads();

        verify(waitingThread, never()).stopThread();
        verify(stoppedThread, never()).stopThread();
    }
}
