package org.orury.batch.job;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.orury.batch.client.KakaoMapClient;
import org.orury.batch.config.JobCompletionNotificationListener;
import org.orury.batch.dto.GymResponse;
import org.orury.batch.dto.KakaoMapGymResponse;
import org.orury.domain.gym.db.model.Gym;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class GymJobConfiguration {
    private final KakaoMapClient kakaoMapClient;
    private final JobRepository jobRepository;
    private final JobCompletionNotificationListener listener;
    private final PlatformTransactionManager transactionManager;
    private final DataSource dataSource;
    private final List<String> locations = List.of(
            "강남구", "강동구", "강북구", "강서구", "관악구",
            "광진구", "구로구", "금천구", "노원구", "도봉구",
            "동대문구", "동작구", "마포구", "서대문구", "서초구",
            "성동구", "성북구", "송파구", "양천구", "영등포구",
            "용산구", "은평구", "종로구", "중구", "중랑구"
    );

    @Bean
    public Job job() throws Exception {
        return new JobBuilder("job", jobRepository)
                .listener(listener)
                .start(step())
                .incrementer(new RunIdIncrementer())
                .build();
    }

    @Bean
    public Step step() throws Exception {
        return new StepBuilder("step", jobRepository)
                .<GymResponse, Gym>chunk(100, transactionManager)
                .reader(itemReader())
                .processor(itemProcessor())
                .writer(itemWriter())
                .build();
    }

    @Bean
    public GymItemReader itemReader() {
        return new GymItemReader(getItems());
    }

    @Bean
    public GymItemProcessor itemProcessor() {
        return new GymItemProcessor();
    }

    @Bean
    public JdbcBatchItemWriter<Gym> itemWriter() throws Exception {
        return new JdbcBatchItemWriterBuilder<Gym>()
                .dataSource(dataSource)
                .sql("INSERT INTO gym (name, road_address, address, latitude, longitude, phone_number, kakao_id, created_at, updated_at) " +
                        "VALUES (:name, :roadAddress, :address, :latitude, :longitude, :phoneNumber, :kakaoId, now(), now()) " +
                        "ON DUPLICATE KEY UPDATE " +
                        "name = :name, " +
                        "road_address = :roadAddress, " +
                        "address = :address, " +
                        "latitude = :latitude, " +
                        "longitude = :longitude, " +
                        "phone_number = :phoneNumber, " +
                        "updated_at = now()"
                )
                .beanMapped()
                .build();
    }

    private List<GymResponse> getItems() {
        List<KakaoMapGymResponse> items = new ArrayList<>();
        locations.forEach(location -> {
            for (int page = 1; page <= 3; page++) {
                var response = kakaoMapClient.searchGyms(location, page);
                items.add(response);
                if (response.getMeta().getIsEnd()) break;
            }
        });

        return items.stream()
                .map(KakaoMapGymResponse::getDocuments)
                .flatMap(List::stream)
                .map(GymResponse::from)
                .collect(Collectors.toList());
    }
}
