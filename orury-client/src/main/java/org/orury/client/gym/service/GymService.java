package org.orury.client.gym.service;

import org.orury.domain.gym.dto.GymDto;
import org.orury.domain.gym.dto.GymLikeDto;

import java.util.List;

public interface GymService {
    GymDto getGymDtoById(Long id);

    List<GymDto> getGymDtosBySearchWordOrderByDistanceAsc(String searchWord, float latitude, float longitude);

    void createGymLike(GymLikeDto gymLikeDto);

    void deleteGymLike(GymLikeDto gymLikeDto);

    boolean isLiked(Long userId, Long gymId);

    void isValidate(Long gymId);

    boolean checkDoingBusiness(GymDto gymDto);
}
