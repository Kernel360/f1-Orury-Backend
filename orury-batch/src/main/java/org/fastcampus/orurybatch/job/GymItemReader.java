package org.fastcampus.orurybatch.job;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.fastcampus.orurybatch.dto.GymResponse;
import org.springframework.batch.item.ItemReader;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class GymItemReader implements ItemReader<GymResponse> {
    private final List<GymResponse> items;

    @Override
    public GymResponse read() {
        if (!items.isEmpty()) {
            return items.remove(0);
        }
        return null;
    }
}
