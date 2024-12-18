package com.hemre.receiverthread.service;

import com.hemre.receiverthread.dto.ThreadDTO;
import com.hemre.receiverthread.thread.BaseThread;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MinimizedThreadInfoService {

    private final CommonListService commonListService;

    @Autowired
    public MinimizedThreadInfoService(CommonListService commonListService) {
        this.commonListService = commonListService;
    }

    public List<ThreadDTO> getThreadInfos() {
        List<ThreadDTO> threadStates = new ArrayList<>();
        ThreadDTO threadDTO;
        for (BaseThread t : commonListService.getThreads()) {

            threadDTO = new ThreadDTO(
                    t.getIndex(),
                    t.getCurrentData(),
                    t.getThreadState(),
                    t.isPriorityChangeable(),
                    t.getType(),
                    t.getPriority()
            );

            threadStates.add(threadDTO);
        }
        return threadStates;
    }

}
