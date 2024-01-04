package org.fastcampus.orurybatch.job;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class GymJobConfiguration {

//  todo  private final GymRepository gymRepository;

//    @Bean
//    public Job job(
//            JobRepository jobRepository,
//            JobCompletionNotificationListener listener,
//            Step step
//    ) {
//        return new JobBuilder("job", jobRepository)
//                .listener(listener)
//                .start(step)
//                .incrementer(new RunIdIncrementer())
//                .build();
//    }
//
//    @Bean
//    public Step step(
//            JobRepository jobRepository,
//            PlatformTransactionManager transactionManager,
//            KakaoResponseItemReader<KakaoResponse> itemReader,
//            GymItemProcessor itemProcessor,
//            JdbcBatchItemWriter<Gym> itemWriter,
//            ) {
//        return new StepBuilder("step", jobRepository)
//                .<KakaoResponse, Gym>chunk(15, transactionManager)
//                .reader(itemReader)
//                .processor(itemProcessor)
//                .writer(itemWriter)
//                .build();
//    }
//
//    @Bean
//    public ItemReader<KakaoResponse> itemReader() {
//        return new KakaoResponseItemReader();
//    }
//
//    @Bean
//    public GymItemProcessor itemProcessor() {
//        return new GymItemProcessor();
//    }
//
//    @Bean
//    public JdbcBatchItemWriter<Gym> itemWriter(DataSource dataSource) {
//        return new JdbcBatchItemWriterBuilder<>()
//                .sql()
//                .dataSource(dataSource)
//                .beanMapped()
//                .build();
//    }


}
