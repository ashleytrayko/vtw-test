package com.example.vtw.batch;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.batch.operations.JobExecutionAlreadyCompleteException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class BatchScheduler {
    private final JobLauncher jobLauncher;
    private final BatchJobConfig batchJobConfig;
    
    
    @Scheduled(cron = "0 0 0 1 1 0") // 5분마다 실행
    public void runJob(){

        Map<String, JobParameter> confMap = new HashMap<>();
        confMap.put("time", new JobParameter(System.currentTimeMillis()));
        JobParameters jobParameters = new JobParameters(confMap);

        try{
            jobLauncher.run(batchJobConfig.batchJob_builder(), jobParameters);
        } catch (JobExecutionAlreadyRunningException | JobExecutionAlreadyCompleteException | JobParametersInvalidException | JobRestartException e){
            log.error("error : {}", e);
        } catch (JobInstanceAlreadyCompleteException e) {
            throw new RuntimeException(e);
        }
    }
}
