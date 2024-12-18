package com.hemre.senderthread.thread;


import com.hemre.senderthread.enums.ThreadStateEnum;
import com.hemre.senderthread.service.CommonListService;
import com.hemre.senderthread.service.StartThreadService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class StartThreadServiceTest {

    private CommonListService commonListService;
    private StartThreadService startThreadService;

    @BeforeEach
    void setUp() {

        commonListService = mock(CommonListService.class);
        startThreadService = new StartThreadService(commonListService);
    }

    @Test
    void startThread_ShouldStartWaitingThread() {

        int index = 0;
        SenderThread waitingThread = mock(SenderThread.class);
        when(waitingThread.getThreadState()).thenReturn(ThreadStateEnum.WAITING);
        when(commonListService.getThread(index)).thenReturn(waitingThread);
        when(commonListService.getSenderThreads()).thenReturn(List.of(waitingThread));

        String result = startThreadService.startThread(index);

        assertEquals("1. Sender Thread started", result);
        verify(waitingThread).start();
    }

    @Test
    void startThread_ShouldReturnError_WhenThreadIsAlreadyRunning() {

        int index = 0;
        SenderThread runningThread = mock(SenderThread.class);
        when(runningThread.getThreadState()).thenReturn(ThreadStateEnum.RUNNING);
        when(commonListService.getThread(index)).thenReturn(runningThread);
        when(commonListService.getSenderThreads()).thenReturn(List.of(runningThread));

        String result = startThreadService.startThread(index);

        assertEquals("Thread already Running", result);
        verify(runningThread, never()).start(); // The start method should not be called
    }

    @Test
    void startThread_ShouldReturnError_WhenThreadIsStopped() {

        int index = 0;
        SenderThread stoppedThread = mock(SenderThread.class);
        when(stoppedThread.getThreadState()).thenReturn(ThreadStateEnum.STOPPED);
        when(commonListService.getThread(index)).thenReturn(stoppedThread);
        when(commonListService.getSenderThreads()).thenReturn(List.of(stoppedThread));

        String result = startThreadService.startThread(index);

        assertEquals("Stopped thread cannot be started directly. Need to restart", result);
        verify(stoppedThread, never()).start();
    }

    @Test
    void startThread_ShouldReturnError_WhenIndexIsInvalid() {

        int invalidIndex = -1;
        when(commonListService.getSenderThreads()).thenReturn(List.of());

        String result = startThreadService.startThread(invalidIndex);

        assertEquals("Invalid index", result);
    }

    @Test
    void startAllThreads_ShouldStartAllWaitingThreads() {

        BaseThread waitingThread1 = mock(BaseThread.class);
        BaseThread waitingThread2 = mock(BaseThread.class);
        when(waitingThread1.getThreadState()).thenReturn(ThreadStateEnum.WAITING);
        when(waitingThread2.getThreadState()).thenReturn(ThreadStateEnum.WAITING);
        when(commonListService.getThreads()).thenReturn(List.of(waitingThread1, waitingThread2));

        startThreadService.startAllThreads();

        verify(waitingThread1).start();
        verify(waitingThread2).start();
    }

    @Test
    void startAllThreads_ShouldNotStartNonWaitingThreads() {

        BaseThread runningThread = mock(BaseThread.class);
        BaseThread stoppedThread = mock(BaseThread.class);
        when(runningThread.getThreadState()).thenReturn(ThreadStateEnum.RUNNING);
        when(stoppedThread.getThreadState()).thenReturn(ThreadStateEnum.STOPPED);
        when(commonListService.getThreads()).thenReturn(List.of(runningThread, stoppedThread));

        startThreadService.startAllThreads();

        verify(runningThread, never()).start();
        verify(stoppedThread, never()).start();
    }
}
