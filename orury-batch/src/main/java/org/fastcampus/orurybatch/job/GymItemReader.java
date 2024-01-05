package org.fastcampus.orurybatch.job;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.fastcampus.orurybatch.dto.GymResponse;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class GymItemReader implements ItemReader<GymResponse> {
    private final List<GymResponse> items;

    @Override
    public GymResponse read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
//        ArrayList<GymResponse> items = new ArrayList<>(this.items);
        log.info("GymItemReader.read()");
        if (!items.isEmpty()) {
            log.info("item : {}", items);
            return items.remove(0);
        }
        return null;
    }
}
