package org.fastcampus.orurybatch.job;

import org.fastcampus.orurydomain.gym.dto.GymDto;
import org.springframework.batch.item.database.JpaItemWriter;

import java.util.List;

public class GymItemWriter extends JpaItemWriter<List<GymDto>> {

}
