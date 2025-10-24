package com.project.shift;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages= {"com.project.shift"})
public class ShiftApplication {

    public static void main(String[] args) {
        SpringApplication.run(ShiftApplication.class, args);
    }

}
