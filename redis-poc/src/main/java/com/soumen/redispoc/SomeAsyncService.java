package com.soumen.redispoc;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.redis.core.ReactiveListOperations;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.Instant;

/**
 * @author Soumen Karmakar
 * @Date 19/01/2022
 */
@Service
@Log4j2
@RequiredArgsConstructor
public class SomeAsyncService {

    private final ReactiveRedisTemplate<String, String> reactiveRedisTemplate;

    @Async
    @SneakyThrows
    public void processTask(Task task) {
        log.info("Kafka-consumer | Picked up - Task - {} for processing ", task.getId());
        ReactiveListOperations<String, String> listOperations = reactiveRedisTemplate.opsForList();
        log.info("Message from Process request : {}", task.getProcessRequest().getMessage());
        Thread.sleep(5000l);
        log.info("processed - Task - {} ", task.getId());
        log.info("Adding the task in Redis : {}", task);
        listOperations.rightPush(task.getId(), "random_status@" + Instant.now().toString()).subscribe();
    }

}
