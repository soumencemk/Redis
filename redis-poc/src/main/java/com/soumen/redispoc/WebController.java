package com.soumen.redispoc;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Soumen Karmakar
 * @Date 19/01/2022
 */
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Log4j2
public class WebController {

    private final StudentRepository studentRepository;

    private final SomeAsyncService someAsyncService;

    private final RedisTemplate redisTemplate;

    @PostMapping("/student")
    public Mono<SampleResponse> addStudent(@RequestBody Student student) {
        log.info("Http Request received");
        List<Student> students = new ArrayList<>();
        students.add(student);
        studentRepository.save(student);
        someAsyncService.doSomething();
        SampleResponse response = SampleResponse.builder().status(SampleResponse.Status.SUCCESS).studentList(students).build();
        return Mono.just(response);
    }

    @GetMapping("/student")
    public Flux<Student> getStudents() {
        return Flux.fromIterable(studentRepository.findAll());
    }
}

@Data
@Builder
class SampleResponse {
    private Status status;
    private List<Student> studentList;

    enum Status {
        SUCCESS, FAILURE
    }
}
