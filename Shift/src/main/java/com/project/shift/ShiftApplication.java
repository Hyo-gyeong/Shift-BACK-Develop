package com.project.shift;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;

@SpringBootApplication
@ComponentScan(basePackages= {"com.project.shift"})

//DB 연결정보 : 프로퍼티 사용
//로컬 경로 / 서버 경로
@PropertySources({
    @PropertySource(value = {
            "file:c:/webservice/configure.properties",
            "file:/usr/local/project/properties/configure.properties"
    }, ignoreResourceNotFound = true)
})

public class ShiftApplication {

    public static void main(String[] args) {
        SpringApplication.run(ShiftApplication.class, args);
    }

}
