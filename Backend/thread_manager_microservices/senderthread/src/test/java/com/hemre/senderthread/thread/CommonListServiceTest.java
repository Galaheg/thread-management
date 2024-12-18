package com.hemre.senderthread.thread;

import com.hemre.senderthread.service.CommonListService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.mock;

class CommonListServiceTest {

    private CommonListService commonListService;

    // run before all @test
    @BeforeEach
    void setUp() {
        commonListService = new CommonListService();
    }

    @Test
    void addThread_ShouldAddBaseThreadToThreads() {
        BaseThread baseThread = mock(BaseThread.class);

        commonListService.addThread(baseThread);

        assertEquals(1, commonListService.getThreads().size());
        assertSame(baseThread, commonListService.getThread(0));
    }

    @Test
    void addThread_ShouldAddSenderThreadToSenders() {
        SenderThread senderThread = mock(SenderThread.class);

        commonListService.addThread(senderThread);


        assertEquals(1, commonListService.getSenderThreads().size());
        assertSame(senderThread, commonListService.getSenderThread(0));
    }

    @Test
    void getThread_ShouldReturnCorrectThread() {

        BaseThread baseThread = mock(BaseThread.class);
        commonListService.addThread(baseThread);

        BaseThread returnedThread = commonListService.getThread(0);

        assertSame(baseThread, returnedThread);
    }

    @Test
    void getSenderThread_ShouldReturnCorrectSenderThread() {

        SenderThread senderThread = mock(SenderThread.class);
        commonListService.addThread(senderThread);

        SenderThread returnedSenderThread = commonListService.getSenderThread(0);

        assertSame(senderThread, returnedSenderThread);
    }

    @Test
    void setSenderThreadIndex_ShouldUpdateSenderThreadAtIndex() {

        SenderThread senderThread = mock(SenderThread.class);
        commonListService.addThread(senderThread);
        SenderThread newSenderThread = mock(SenderThread.class);

        commonListService.setSenderThreadIndex(0, newSenderThread);

        assertSame(newSenderThread, commonListService.getSenderThread(0));
    }

    @Test
    void setBaseThreadIndex_ShouldUpdateBaseThreadAtIndex() {

        BaseThread baseThread = mock(BaseThread.class);
        commonListService.addThread(baseThread);
        SenderThread newBaseThread = mock(SenderThread.class);

        commonListService.setBaseThreadIndex(0, newBaseThread);

        assertSame(newBaseThread, commonListService.getThread(0));
    }

    @Test
    void addEmitter_ShouldAddEmitterToEmitters() {

        SseEmitter emitter = mock(SseEmitter.class);

        commonListService.addEmitter(emitter);

        assertEquals(1, commonListService.getEmitters().size());
        assertSame(emitter, commonListService.getEmitters().get(0));
    }

    @Test
    void removeEmitter_ShouldRemoveEmitterFromEmitters() {

        SseEmitter emitter = mock(SseEmitter.class);
        commonListService.addEmitter(emitter);

        commonListService.removeEmitter(emitter);

        assertEquals(0, commonListService.getEmitters().size());
    }

    @Test
    void getEmitters_ShouldReturnAllEmitters() {

        SseEmitter emitter1 = mock(SseEmitter.class);
        SseEmitter emitter2 = mock(SseEmitter.class);
        commonListService.addEmitter(emitter1);
        commonListService.addEmitter(emitter2);

        List<SseEmitter> emitters = commonListService.getEmitters();

        assertEquals(2, emitters.size());
        assertSame(emitter1, emitters.get(0));
        assertSame(emitter2, emitters.get(1));
    }
}
