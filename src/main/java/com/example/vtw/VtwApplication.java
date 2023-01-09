package com.example.vtw;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@EnableBatchProcessing // 배치 사용을 위한 선언
@SpringBootApplication
public class VtwApplication {

    public static void main(String[] args) {
        SpringApplication.run(VtwApplication.class, args);
    }

}
