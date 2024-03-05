package org.orury.client.crew.interfaces.response;

import org.orury.client.global.IdIdentifiable;
import org.orury.domain.crew.domain.dto.CrewDto;
import org.orury.domain.user.domain.dto.UserDto;

import java.time.LocalDateTime;

public record CrewsResponseByMyCrew(
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
        // new 표시 -> 최근 일정 등록 여부
) implements IdIdentifiable {
    public static CrewsResponseByMyCrew of(
            CrewDto crewDto
    ) {
        return new CrewsResponseByMyCrew(
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
