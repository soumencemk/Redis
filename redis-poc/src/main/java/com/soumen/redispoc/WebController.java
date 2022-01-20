package com.soumen.redispoc;

import lombok.*;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.redis.core.ReactiveHashOperations;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.Serializable;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static com.soumen.redispoc.Util.REDIS_KEY_TASK;

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
    private final ReactiveRedisTemplate<String, String> reactiveRedisTemplate;


    @SneakyThrows
    @PostMapping("/process")
    public Mono<SampleResponse> addTask(@RequestBody ProcessRequest processRequest) {

        log.info("Http Request received : {}", processRequest);
        Task task = buildTask(processRequest);
        someAsyncService.processTask(task);
        log.info("Pushed task {} in Kafka", task.getId());
        List<ByteBuffer> taskIDBBufList = List.of(ByteBuffer.wrap(task.getId().getBytes(StandardCharsets.UTF_8)));
        Mono<SampleResponse> responseMono = reactiveRedisTemplate.getConnectionFactory()
                .getReactiveConnection()
                .listCommands()
                .brPop(taskIDBBufList, Duration.of(10, ChronoUnit.SECONDS))
                .map(popResult -> Mono.just(SampleResponse.builder().status(Charset.defaultCharset().decode(popResult.getValue()).toString()).build()))
                .flatMap(sampleResponseMono -> sampleResponseMono);
        log.info("Waiting for BRPOP....");
        return responseMono;
    }

    private SampleResponse testResponse() {
        return SampleResponse.builder().status("SUCCESS").build();
    }

    private Task buildTask(ProcessRequest processRequest) {
        return Task.builder()
                .id(UUID.randomUUID().toString())
                .processRequest(processRequest)
                .build();
    }

    @GetMapping("/task")
    public Flux<Map.Entry<String, Task>> getTasks() {
        ReactiveHashOperations<String, String, Task> reactiveHashOperations = reactiveRedisTemplate.opsForHash();
        Flux<Map.Entry<String, Task>> entries = reactiveHashOperations.entries(REDIS_KEY_TASK);
        return entries;
    }
}


@Data
@Builder
class SampleResponse {
    private String status;
}

@ToString
@Data
class ProcessRequest {
    private String message;
}

@Data
@Getter
@Setter
@Builder
@ToString
class Task implements Serializable {
    private String id;
    private ProcessRequest processRequest;
    private SampleResponse sampleResponse;
}

