package org.fastcampus.orurybatch.job;

import lombok.extern.slf4j.Slf4j;
import org.fastcampus.orurybatch.dto.GymResponse;
import org.fastcampus.orurydomain.gym.db.model.Gym;
import org.springframework.batch.item.ItemProcessor;

@Slf4j
public class GymItemProcessor implements ItemProcessor<GymResponse, Gym> {
    @Override
    public Gym process(GymResponse item) {
        return item.toDto().toEntity();
    }
}
