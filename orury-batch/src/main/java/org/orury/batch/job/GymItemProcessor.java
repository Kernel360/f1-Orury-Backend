package org.orury.batch.job;

import lombok.extern.slf4j.Slf4j;
import org.orury.batch.dto.GymResponse;
import org.orury.domain.gym.db.model.Gym;
import org.springframework.batch.item.ItemProcessor;

@Slf4j
public class GymItemProcessor implements ItemProcessor<GymResponse, Gym> {
    @Override
    public Gym process(GymResponse item) throws Exception {
        return item.toDto().toEntity();
    }
}
