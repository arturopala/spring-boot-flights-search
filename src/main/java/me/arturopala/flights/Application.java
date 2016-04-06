package me.arturopala.flights;

import org.jsondoc.spring.boot.starter.EnableJSONDoc;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableJSONDoc
public class Application {
    public static void main(final String[] args) {
        SpringApplication.run(Application.class, args);
    }
}


