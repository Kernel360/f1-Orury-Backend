package org.orurybatch.job;

import lombok.extern.slf4j.Slf4j;
import org.orurybatch.dto.GymResponse;
import org.orurydomain.gym.db.model.Gym;
import org.springframework.batch.item.ItemProcessor;

@Slf4j
public class GymItemProcessor implements ItemProcessor<GymResponse, Gym> {
    @Override
    public Gym process(GymResponse item) throws Exception {
        return item.toDto().toEntity();
    }
}
