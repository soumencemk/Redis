package com.soumen.redispoc;

import lombok.*;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.Date;
import java.util.UUID;

enum Status {
    SUCCESS, FAILURE
}

/**
 * @author Soumen Karmakar
 * @Date 19/01/2022
 */
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Log4j2
public class WebController {

    private final SomeAsyncService someAsyncService;
    private final TaskRepository taskRepository;

    @PostMapping("/process")
    public Mono<SampleResponse> addTask(@RequestBody ProcessRequest processRequest) {
        Long startTime = Instant.now().toEpochMilli();
        log.info("Http Request received : {}", processRequest);
        Task task = buildTask(processRequest);
        taskRepository.save(task);
        String taskID = task.getId();
        log.info("Task - {} saved successfully", taskID);
        someAsyncService.processTask(task);
        log.info("Waiting for response...");
        while (true) {
            task = taskRepository.findById(taskID).orElse(null);
            if (task != null && task.getSampleResponse() != null) {
                log.info("Processing finished, time took : {} ms.", (Instant.now().toEpochMilli() - startTime));
                taskRepository.deleteById(taskID);
                break;
            }
        }
        return Mono.just(task.getSampleResponse());
    }

    private Task buildTask(ProcessRequest processRequest) {
        return Task.builder()
                .id(UUID.randomUUID().toString())
                .processRequest(processRequest)
                .startDate(new Date())
                .build();
    }

    @GetMapping("/task")
    public Flux<Task> getTasks() {
        return Flux.fromIterable(taskRepository.findAll());
    }
}


@Data
@Builder
class SampleResponse {
    private Status status;
}

@ToString
class ProcessRequest {
    private String message;
}

@RedisHash("task")
@Data
@Getter
@Setter
@Builder
@ToString
class Task {
    @Indexed
    @Id
    private String id;
    private ProcessRequest processRequest;
    private Date startDate;
    private Date endDate;
    private Status status;
    private String failureReason;
    private SampleResponse sampleResponse;
}

