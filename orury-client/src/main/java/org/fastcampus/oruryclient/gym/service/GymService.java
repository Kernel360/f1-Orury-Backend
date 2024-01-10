package org.fastcampus.oruryclient.gym.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.fastcampus.orurycommon.error.code.GymErrorCode;
import org.fastcampus.orurycommon.error.exception.BusinessException;
import org.fastcampus.orurydomain.gym.db.model.Gym;
import org.fastcampus.orurydomain.gym.db.repository.GymRepository;
import org.fastcampus.orurydomain.gym.dto.GymDto;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class GymService {
    private final GymRepository gymRepository;

    public GymDto getGymDtoById(Long id) {
        Gym gym = gymRepository.findById(id).orElseThrow(() -> new BusinessException(GymErrorCode.NOT_FOUND));
        return GymDto.from(gym);
    }
}
