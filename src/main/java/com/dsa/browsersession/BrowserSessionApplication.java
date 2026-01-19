package com.dsa.browsersession;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class BrowserSessionApplication {

    public static void main(String[] args) {
        SpringApplication.run(BrowserSessionApplication.class, args);
    }

}
