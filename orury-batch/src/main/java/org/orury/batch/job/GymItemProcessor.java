package org.orury.batch.job;

import lombok.extern.slf4j.Slf4j;
import org.orury.batch.dto.GymResponse;
import org.orury.domain.gym.domain.entity.Gym;
import org.springframework.batch.item.ItemProcessor;

@Slf4j
public class GymItemProcessor implements ItemProcessor<GymResponse, Gym> {
    @Override
    public Gym process(GymResponse item) {
        return item.toDto().toEntity();
    }
}
