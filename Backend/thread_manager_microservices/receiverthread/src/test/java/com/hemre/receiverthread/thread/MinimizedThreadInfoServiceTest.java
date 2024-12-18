package com.hemre.receiverthread.thread;

import com.hemre.receiverthread.dto.ThreadDTO;
import com.hemre.receiverthread.enums.ThreadStateEnum;
import com.hemre.receiverthread.enums.ThreadTypeEnum;
import com.hemre.receiverthread.service.CommonListService;
import com.hemre.receiverthread.service.MinimizedThreadInfoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class MinimizedThreadInfoServiceTest {

    private CommonListService commonListService;
    private MinimizedThreadInfoService minimizedThreadInfoService;

    @BeforeEach
    void setUp() {
        commonListService = Mockito.mock(CommonListService.class);
        minimizedThreadInfoService = new MinimizedThreadInfoService(commonListService);
    }

    @Test
    void getThreadInfos_ShouldReturnThreadDTOList() {

        BaseThread thread1 = mock(BaseThread.class);
        BaseThread thread2 = mock(BaseThread.class);

        when(thread1.getIndex()).thenReturn(0);
        when(thread1.getCurrentData()).thenReturn("Data1");
        when(thread1.getThreadState()).thenReturn(ThreadStateEnum.RUNNING);
        when(thread1.isPriorityChangeable()).thenReturn(true);
        when(thread1.getType()).thenReturn(ThreadTypeEnum.RECEIVER);
        when(thread1.getPriority()).thenReturn(5);

        when(thread2.getIndex()).thenReturn(1);
        when(thread2.getCurrentData()).thenReturn("Data2");
        when(thread2.getThreadState()).thenReturn(ThreadStateEnum.WAITING);
        when(thread2.isPriorityChangeable()).thenReturn(false);
        when(thread2.getType()).thenReturn(ThreadTypeEnum.SENDER);
        when(thread2.getPriority()).thenReturn(3);

        when(commonListService.getThreads()).thenReturn(Arrays.asList(thread1, thread2));

        List<ThreadDTO> threadInfos = minimizedThreadInfoService.getThreadInfos();

        assertEquals(2, threadInfos.size());

        ThreadDTO dto1 = threadInfos.get(0);
        assertEquals(0, dto1.getIndex());
        assertEquals("Data1", dto1.getCurrentData());
        assertEquals(ThreadStateEnum.RUNNING, dto1.getThreadState());
        assertEquals(true, dto1.isPriorityChangeable());
        assertEquals(ThreadTypeEnum.RECEIVER, dto1.getType());
        assertEquals(5, dto1.getPriority());

        ThreadDTO dto2 = threadInfos.get(1);
        assertEquals(1, dto2.getIndex());
        assertEquals("Data2", dto2.getCurrentData());
        assertEquals(ThreadStateEnum.WAITING, dto2.getThreadState());
        assertEquals(false, dto2.isPriorityChangeable());
        assertEquals(ThreadTypeEnum.SENDER, dto2.getType());
        assertEquals(3, dto2.getPriority());

        verify(commonListService, times(1)).getThreads();
    }
}

