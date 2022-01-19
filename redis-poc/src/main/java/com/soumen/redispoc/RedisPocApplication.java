package com.soumen.redispoc;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@RequiredArgsConstructor
@Log4j2
@EnableAsync
public class RedisPocApplication {

    public static void main(String[] args) {
        SpringApplication.run(RedisPocApplication.class, args);
    }

}
