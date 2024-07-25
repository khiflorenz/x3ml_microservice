package net.artresearch.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class X3mlApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(X3mlApiApplication.class, args);
    }
}

