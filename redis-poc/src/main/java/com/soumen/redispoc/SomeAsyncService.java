package com.soumen.redispoc;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @author Soumen Karmakar
 * @Date 19/01/2022
 */
@Service
@Log4j2
@RequiredArgsConstructor
public class SomeAsyncService {
    private final TaskRepository taskRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Async
    @SneakyThrows
    public void processTask(Task task) {
        log.info("Picked up - Task - {} for processing ", task.getId());
        Thread.sleep(5000l);
        log.info("processed - Task - {} ", task.getId());
        Task task1 = taskRepository.findById(task.getId()).get();
        task1.setEndDate(new Date());
        task1.setStatus(Status.SUCCESS);
        task1.setSampleResponse(SampleResponse.builder().status(Status.SUCCESS).build());
        log.info("Updating the task : {}", task1);
        taskRepository.save(task1);
    }
}
