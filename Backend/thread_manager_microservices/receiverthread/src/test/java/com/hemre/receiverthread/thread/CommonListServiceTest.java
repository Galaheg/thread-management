package com.hemre.receiverthread.thread;

import com.hemre.receiverthread.service.CommonListService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;
import java.util.concurrent.CountDownLatch;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CommonListServiceTest {

    private CommonListService commonListService;

    @BeforeEach
    void setUp() {
        commonListService = new CommonListService();
    }

    @Test
    void addThread_ShouldAddBaseThreadAndReceiverThread() {

        BaseThread baseThread = Mockito.mock(BaseThread.class);
        ReceiverThread receiverThread = new ReceiverThread(1, true, new CountDownLatch(1));

        commonListService.addThread(baseThread);
        commonListService.addThread(receiverThread);

        List<BaseThread> threads = commonListService.getThreads();
        List<ReceiverThread> receivers = commonListService.getReceiverThreads();

        assertEquals(2, threads.size());
        assertEquals(1, receivers.size());
        assertTrue(threads.contains(baseThread));
        assertTrue(threads.contains(receiverThread));
        assertTrue(receivers.contains(receiverThread));
    }

    @Test
    void getThread_ShouldReturnCorrectThreadByIndex() {

        BaseThread baseThread = Mockito.mock(BaseThread.class);
        commonListService.addThread(baseThread);

        BaseThread retrievedThread = commonListService.getThread(0);

        assertEquals(baseThread, retrievedThread);
    }

    @Test
    void getReceiverThread_ShouldReturnCorrectReceiverThreadByIndex() {

        ReceiverThread receiverThread = new ReceiverThread(1, true, new CountDownLatch(1));
        commonListService.addThread(receiverThread);

        ReceiverThread retrievedThread = commonListService.getReceiverThread(0);

        assertEquals(receiverThread, retrievedThread);
    }

    @Test
    void setReceiverThreadIndex_ShouldUpdateReceiverThreadAtIndex() {

        ReceiverThread oldThread = new ReceiverThread(1, true, new CountDownLatch(1));
        ReceiverThread newThread = new ReceiverThread(2, false, new CountDownLatch(1));
        commonListService.addThread(oldThread);

        commonListService.setReceiverThreadIndex(0, newThread);

        ReceiverThread updatedThread = commonListService.getReceiverThread(0);
        assertEquals(newThread, updatedThread);
    }

    @Test
    void setBaseThreadIndex_ShouldUpdateBaseThreadAtIndex() {

        BaseThread oldThread = Mockito.mock(BaseThread.class);
        ReceiverThread newThread = new ReceiverThread(1, true, new CountDownLatch(1));
        commonListService.addThread(oldThread);

        commonListService.setBaseThreadIndex(0, newThread);

        BaseThread updatedThread = commonListService.getThread(0);
        assertEquals(newThread, updatedThread);
    }

    @Test
    void addEmitter_ShouldAddEmitterToEmittersList() {

        SseEmitter emitter = new SseEmitter();

        commonListService.addEmitter(emitter);

        List<SseEmitter> emitters = commonListService.getEmitters();
        assertEquals(1, emitters.size());
        assertTrue(emitters.contains(emitter));
    }

    @Test
    void removeEmitter_ShouldRemoveEmitterFromEmittersList() {

        SseEmitter emitter = new SseEmitter();
        commonListService.addEmitter(emitter);

        commonListService.removeEmitter(emitter);

        List<SseEmitter> emitters = commonListService.getEmitters();
        assertTrue(emitters.isEmpty());
    }
}

