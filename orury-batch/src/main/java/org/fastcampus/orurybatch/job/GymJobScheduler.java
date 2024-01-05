package org.fastcampus.orurybatch.job;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class GymJobScheduler {
    private final JobLauncher jobLauncher;
    private final GymJobConfiguration gymJobConfiguration;
    
    @Scheduled(cron = "0 0 0 * * *")
    public void run() throws Exception {
        JobParameters jobParameters = new JobParametersBuilder()
                .addLong("time", System.currentTimeMillis())
                .toJobParameters();
        jobLauncher.run(gymJobConfiguration.job(), jobParameters);
    }
}
