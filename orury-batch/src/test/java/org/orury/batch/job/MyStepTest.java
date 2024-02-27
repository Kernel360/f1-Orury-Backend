package org.orury.batch.job;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.orury.batch.client.KakaoMapClient;
import org.orury.batch.dto.GymResponse;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.JobRepositoryTestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@Disabled
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {GymJobConfiguration.class})
@DisplayName("Job Step 실행 테스트")
class MyStepTest {
    @MockBean
    private KakaoMapClient kakaoMapClient;
    private final JobLauncherTestUtils jobLauncherTestUtils;
    private final JobRepositoryTestUtils jobRepositoryTestUtils;

    public MyStepTest(
            @Autowired JobLauncherTestUtils jobLauncherTestUtils,
            @Autowired JobRepositoryTestUtils jobRepositoryTestUtils
    ) {
        this.jobLauncherTestUtils = jobLauncherTestUtils;
        this.jobRepositoryTestUtils = jobRepositoryTestUtils;
    }

//    @BeforeEach
//    public void setup(
//            @Autowired @Qualifier("job") Job job,
//            @Autowired @Qualifier("jobRepository") JobRepository jobRepository
//    ) {
//        this.jobLauncherTestUtils.setJob(job);
//        this.jobRepositoryTestUtils.setJobRepository(jobRepository);
//    }
//
//    @AfterEach
//    public void cleanUp() {
//        this.jobRepositoryTestUtils.removeJobExecutions();
//    }

    @Test
    void testMyStep() throws Exception {
        var expected = List.of(createResponse());
        when(kakaoMapClient.getItems()).thenReturn(expected);

        JobExecution jobExecution = jobLauncherTestUtils.launchJob();

        // Step 실행 성공 여부 검증
        assertEquals(BatchStatus.COMPLETED, jobExecution.getStepExecutions()
                .iterator()
                .next()
                .getStatus());
    }

    private GymResponse createResponse() {
        return GymResponse.of(
                "placeName",
                "kakaoId",
                "roadAddressName",
                "phone",
                "y",
                "x",
                "addressName"
        );
    }
}