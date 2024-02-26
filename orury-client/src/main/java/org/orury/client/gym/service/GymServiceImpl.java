package org.orury.client.gym.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.orury.common.error.code.GymErrorCode;
import org.orury.common.error.exception.BusinessException;
import org.orury.common.util.BusinessHoursConverter;
import org.orury.common.util.ImageUtils;
import org.orury.common.util.S3Folder;
import org.orury.domain.gym.GymReader;
import org.orury.domain.gym.GymStore;
import org.orury.domain.gym.db.model.Gym;
import org.orury.domain.gym.dto.GymDto;
import org.orury.domain.gym.dto.GymLikeDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class GymServiceImpl implements GymService {
    private final GymReader gymReader;
    private final GymStore gymStore;
    private final ImageUtils imageUtils;

    @Override
    @Transactional(readOnly = true)
    public GymDto getGymDtoById(Long id) {
        var gym = gymReader.findGymById(id);
        var urls = imageUtils.getUrls(S3Folder.GYM.getName(), gym.getImages());
        return GymDto.from(gym, urls);
    }

    @Override
    @Transactional(readOnly = true)
    public List<GymDto> getGymDtosBySearchWordOrderByDistanceAsc(String searchWord, float latitude, float longitude) {
        var gyms = gymReader.findGymsBySearchWord(searchWord);
        return sortGymsByDistanceAsc(gyms, latitude, longitude);
    }

    @Override
    @Transactional
    public void createGymLike(GymLikeDto gymLikeDto) {
        if (gymReader.existGymLikeById(gymLikeDto.gymLikePK())) return;
        gymStore.saveGymLike(gymLikeDto.toEntity());
        gymStore.increaseLikeCount(gymLikeDto.gymLikePK().getGymId());
    }

    @Override
    @Transactional
    public void deleteGymLike(GymLikeDto gymLikeDto) {
        if (!gymReader.existGymLikeById(gymLikeDto.gymLikePK())) return;
        gymStore.deleteGymLike(gymLikeDto.toEntity());
        gymStore.decreaseLikeCount(gymLikeDto.gymLikePK().getGymId());
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isLiked(Long userId, Long gymId) {
        return gymReader.existsGymLikeByUserIdAndGymId(userId, gymId);
    }

    @Override
    @Transactional(readOnly = true)
    public void isValidate(Long gymId) {
        if (!gymReader.existsGymById(gymId)) throw new BusinessException(GymErrorCode.NOT_FOUND);
    }

    @Override
    public boolean checkDoingBusiness(GymDto gymDto) {
        DayOfWeek today = LocalDateTime.now().getDayOfWeek();
        String businessHour = gymDto.businessHours().get(today);

        if (businessHour == null)
            return false;

        LocalTime openTime = BusinessHoursConverter.extractOpenTime(businessHour);
        LocalTime closeTime = BusinessHoursConverter.extractCloseTime(businessHour);
        LocalTime nowTime = LocalTime.now();

        return nowTime.isAfter(openTime) && nowTime.isBefore(closeTime);
    }

    private List<GymDto> sortGymsByDistanceAsc(List<Gym> gyms, float latitude, float longitude) {
        return gyms.stream()
                .map(it -> GymDto.from(it, imageUtils.getUrls(S3Folder.GYM.getName(), it.getImages())))
                .sorted((g1, g2) -> {
                    double distance1 = getDistance(latitude, longitude, g1.latitude(), g1.longitude());
                    double distance2 = getDistance(latitude, longitude, g2.latitude(), g2.longitude());
                    return (distance1 < distance2) ? -1 : 1;
                })
                .toList();
    }

    private double getDistance(double latitude1, double longitude1, double latitude2, double longitude2) {
        double x = Math.pow(latitude2 - latitude1, 2);
        double y = Math.pow(longitude2 - longitude1, 2);
        return Math.sqrt(x + y);
    }
}
