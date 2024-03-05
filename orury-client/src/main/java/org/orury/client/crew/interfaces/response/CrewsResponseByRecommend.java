package org.orury.client.crew.interfaces.response;

import org.orury.client.global.IdIdentifiable;
import org.orury.domain.crew.domain.dto.CrewDto;
import org.orury.domain.user.domain.dto.UserDto;

import java.time.LocalDateTime;

public record CrewsResponseByRecommend(
        Long id,
        String name,
        int memberCount,
        int capacity,
        String region,
        String icon,
        UserDto userDto,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
        // 운영자 포함 4명까지의 프로필 이미지
        // 최근 대화 시간
) implements IdIdentifiable {
    public static CrewsResponseByRecommend of(
            CrewDto crewDto
    ) {
        return new CrewsResponseByRecommend(
                crewDto.id(),
                crewDto.name(),
                crewDto.memberCount(),
                crewDto.capacity(),
                crewDto.region(),
                crewDto.icon(),
                crewDto.userDto(),
                crewDto.createdAt(),
                crewDto.updatedAt()
        );
    }
}
