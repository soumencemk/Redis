package com.soumen.redispoc;

import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * @author Soumen Karmakar
 * @Date 19/01/2022
 */
@Service
@Log4j2
public class SomeAsyncService {
    @Async
    public void doSomething() {
        log.info("Doing something...");

    }
}
