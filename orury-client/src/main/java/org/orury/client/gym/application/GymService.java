package org.orury.client.gym.application;

import org.orury.client.gym.interfaces.request.AreaGrid;
import org.orury.domain.gym.domain.dto.GymDto;
import org.orury.domain.gym.domain.dto.GymLikeDto;

import java.util.List;

public interface GymService {
    GymDto getGymDtoById(Long id);

    List<GymDto> getGymDtosBySearchWordOrderByDistanceAsc(String searchWord, float latitude, float longitude);

    List<GymDto> getGymDtosByAreaGridOrderByDistanceAsc(AreaGrid areaGrid, float latitude, float longitude);

    void createGymLike(GymLikeDto gymLikeDto);

    void deleteGymLike(GymLikeDto gymLikeDto);

    boolean isLiked(Long userId, Long gymId);

    boolean checkDoingBusiness(GymDto gymDto);
}
