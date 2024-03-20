package org.orury.batch.job;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.orury.batch.dto.GymResponse;
import org.orury.domain.gym.domain.entity.Gym;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.orury.batch.BatchFixtureFactory.TestGymResponse.createGymResponse;
import static org.orury.domain.GymDomainFixture.TestGym.createGym;

@ExtendWith(MockitoExtension.class)
@DisplayName("Step 아이템 관련 테스트")
class StepItemTest {
    @Mock
    private ItemReader<GymResponse> itemReader;
    @Mock
    private ItemProcessor<GymResponse, Gym> itemProcessor;
    @Mock
    private ItemWriter<Gym> itemWriter;

    @DisplayName("Step Item Reader Test")
    @Test
    void when_GymResponse_Then_ItemReader_Successfully() throws Exception {
        // ItemReader가 데이터를 정상적으로 읽는지 검증
        var response = createGymResponse().build().get();
        when(itemReader.read()).thenReturn(response);

        assertThat(itemReader.read())
                .isNotNull()
                .isEqualTo(response);
    }

    @DisplayName("Step Item Processor Test")
    @Test
    void when_GymResponse_Then_ItemProcessor_Successfully() throws Exception {
        // ItemProcessor가 데이터를 정상적으로 처리하는지 검증
        var preItem = createGymResponse().build().get();
        var expected = createGym().build().get();

        when(itemProcessor.process(preItem)).thenReturn(expected);

        Gym actual = itemProcessor.process(preItem);

        assertEquals(actual, expected);
    }

    @DisplayName("Step Item Writer Test")
    @Test
    void when_GymResponse_Then_ItemWriter_Successfully() throws Exception {
        var expected = Chunk.of(createGym().build().get());
        itemWriter.write(Chunk.of(createGym().build().get()));
        verify(itemWriter, times(1)).write(expected);
    }
}