package org.fastcampus.orurybatch.job;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.fastcampus.orurybatch.config.JobCompletionNotificationListener;
import org.fastcampus.orurybatch.dto.KakaoMapGymResponse;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class GymJobConfiguration {
    private final KakaoMapClient kakaoMapClient;

    @Bean
    public Job job(
            JobRepository jobRepository,
            JobCompletionNotificationListener listener,
            Step step
    ) {
        return new JobBuilder("job", jobRepository)
                .listener(listener)
                .start(step)
                .incrementer(new RunIdIncrementer())
                .build();
    }

    @Bean
    public Step step(
            JobRepository jobRepository,
            PlatformTransactionManager transactionManager,
            GymItemReader itemReader,
//            GymItemProcessor itemProcessor,
            ItemWriter<KakaoMapGymResponse> itemWriter
    ) {
        return new StepBuilder("step", jobRepository)
                .<KakaoMapGymResponse, KakaoMapGymResponse>chunk(1, transactionManager)
                .reader(itemReader)
//                .processor(itemProcessor)
                .writer(itemWriter)
                .build();
    }

    @Bean
    public GymItemReader itemReader() {
        log.info("itemReader()");
        return new GymItemReader(kakaoMapClient);
    }

//    @Bean
//    public GymItemProcessor itemProcessor() {
//        return new GymItemProcessor();
//    }

    @Bean
    public ItemWriter<KakaoMapGymResponse> itemWriter() {
        return items -> {
            for (KakaoMapGymResponse item : items) {
                item.getDocuments().forEach(doc -> {
                    log.info("item = {}", doc.getPlace_name());
                });
            }
        };
    }
//    @Bean
//    public JpaItemWriter<List<GymDto>> itemWriter(
//            EntityManagerFactory entityManagerFactory
//    ) throws Exception {
//        return new JpaItemWriterBuilder<List<GymDto>>()
//                .entityManagerFactory(entityManagerFactory)
//                .build();
//    }

}
