package org.orury.batch.job;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.orury.batch.client.KakaoMapClient;
import org.orury.batch.config.JobCompletionNotificationListener;
import org.orury.batch.dto.GymResponse;
import org.orury.batch.util.SqlQuery;
import org.orury.domain.gym.domain.entity.Gym;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class GymJobConfiguration {
    private final KakaoMapClient kakaoMapClient;

    @Bean
    public Job job(
            JobRepository jobRepository,
            @Qualifier("kakaoClientStep") Step step
    ) {
        return new JobBuilder("job", jobRepository)
                .listener(new JobCompletionNotificationListener())
                .start(step)
                .incrementer(new RunIdIncrementer())
                .build();
    }

    @Bean
    public Step kakaoClientStep(
            JobRepository jobRepository,
            PlatformTransactionManager transactionManager,
            DataSource dataSource
    ) {
        return new StepBuilder("kakaoClientStep", jobRepository)
                .<GymResponse, Gym>chunk(100, transactionManager)
                .reader(itemReader())
                .processor(itemProcessor())
                .writer(itemWriter(dataSource))
                .build();
    }

    @Bean
    public GymItemReader itemReader() {
        return new GymItemReader(kakaoMapClient.getItems());
    }

    @Bean
    public GymItemProcessor itemProcessor() {
        return new GymItemProcessor();
    }

    @Bean
    public ItemWriter<Gym> itemWriter(DataSource dataSource) {
        return new JdbcBatchItemWriterBuilder<Gym>()
                .dataSource(dataSource)
                .sql(SqlQuery.INSERT_GYM)
                .beanMapped()
                .build();
    }
}