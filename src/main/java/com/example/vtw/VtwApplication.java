package com.example.vtw;

import com.example.vtw.batch.BatchJobConfig;
import org.springframework.batch.core.configuration.annotation.BatchConfigurationSelector;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling   // 스케줄링을 위한 선언
@EnableBatchProcessing // 배치 사용을 위한 선언
@SpringBootApplication
public class VtwApplication {
    public static void main(String[] args) {
        SpringApplication.run(VtwApplication.class, args);
    }

}

