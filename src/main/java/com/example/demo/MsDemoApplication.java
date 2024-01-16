package com.example.demo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@SpringBootApplication
public class MsDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(MsDemoApplication.class, args);
        log.info("service up & ready");
    }

}
