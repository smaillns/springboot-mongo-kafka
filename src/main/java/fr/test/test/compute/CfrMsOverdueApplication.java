package fr.test.test.compute;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@SpringBootApplication
public class CfrMsOverdueApplication {

    public static void main(String[] args) {
        SpringApplication.run(CfrMsOverdueApplication.class, args);
        log.info("cfr-overdue up & ready");
    }

}
