package org.orury.client.crew.interfaces.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.orury.client.global.IdIdentifiable;
import org.orury.domain.crew.domain.dto.CrewDto;
import org.orury.domain.global.domain.Region;

import java.time.LocalDateTime;
import java.util.List;

public record CrewsResponseByRank(
        Long id,
        String name,
        int memberCount,
        int capacity,
        Region region,
        String icon,
        String headProfileImage,
        @JsonFormat(shape = JsonFormat.Shape.STRING)
        LocalDateTime createdAt,
        @JsonFormat(shape = JsonFormat.Shape.STRING)
        LocalDateTime updatedAt,
        List<String> tags
        // TODO: int rank
        // TODO: 운영자 포함 4명까지의 프로필 이미지
) implements IdIdentifiable {
    public static CrewsResponseByRank of(
            CrewDto crewDto
    ) {
        return new CrewsResponseByRank(
                crewDto.id(),
                crewDto.name(),
                crewDto.memberCount(),
                crewDto.capacity(),
                crewDto.region(),
                crewDto.icon(),
                crewDto.userDto().profileImage(),
                crewDto.createdAt(),
                crewDto.updatedAt(),
                crewDto.tags()
        );
    }
}
// TODO: 추가로 여기에 순위 달아야할 듯? -> cursor로 조회된 값 기반으로 순서대로 번호매기기?



