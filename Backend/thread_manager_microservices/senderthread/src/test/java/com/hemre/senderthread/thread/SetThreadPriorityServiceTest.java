package com.hemre.senderthread.thread;


import com.hemre.senderthread.service.CommonListService;
import com.hemre.senderthread.service.SetThreadPriorityService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class SetThreadPriorityServiceTest {

    private CommonListService commonListService;
    private SetThreadPriorityService setThreadPriorityService;

    @BeforeEach
    void setUp() {
        commonListService = mock(CommonListService.class);
        setThreadPriorityService = new SetThreadPriorityService(commonListService);
    }

    @Test
    void setThreadPriority_ShouldSetPriorityForChangeableThread() {

        int index = 0;
        int newPriority = 5;
        BaseThread thread = mock(BaseThread.class);

        when(commonListService.getThreads()).thenReturn(List.of(thread));
        when(commonListService.getThread(index)).thenReturn(thread);
        when(thread.isPriorityChangeable()).thenReturn(true);

        String result = setThreadPriorityService.setThreadPriority(index, newPriority);

        assertEquals("0. Sender thread's priority set to 5", result);
        verify(thread, times(1)).setPriority(newPriority);
    }

    @Test
    void setThreadPriority_ShouldReturnErrorForNonChangeableThread() {

        int index = 0;
        int newPriority = 5;
        BaseThread thread = mock(BaseThread.class);

        when(commonListService.getThreads()).thenReturn(List.of(thread));
        when(commonListService.getThread(index)).thenReturn(thread);
        when(thread.isPriorityChangeable()).thenReturn(false);

        String result = setThreadPriorityService.setThreadPriority(index, newPriority);

        assertEquals("Thread is not changeable", result);
        verify(thread, never()).setPriority(anyInt());
    }

    @Test
    void setThreadPriority_ShouldReturnErrorForInvalidPriority() {

        int index = 0;
        int invalidPriority = 11; // Priority out of valid range
        BaseThread thread = mock(BaseThread.class);

        when(commonListService.getThreads()).thenReturn(List.of(thread));
        when(commonListService.getThread(index)).thenReturn(thread);
        when(thread.isPriorityChangeable()).thenReturn(true);

        String result = setThreadPriorityService.setThreadPriority(index, invalidPriority);

        assertEquals("Thread is not changeable", result);
        verify(thread, never()).setPriority(anyInt());
    }

    @Test
    void setThreadPriority_ShouldReturnErrorForInvalidIndex() {

        int invalidIndex = -1;

        when(commonListService.getThreads()).thenReturn(new ArrayList<>());

        String result = setThreadPriorityService.setThreadPriority(invalidIndex, 5);

        assertEquals("Please enter a valid index", result);
    }
}
