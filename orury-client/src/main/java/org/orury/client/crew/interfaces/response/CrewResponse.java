package org.orury.client.crew.interfaces.response;

import com.fasterxml.jackson.annotation.JsonFormat;

import org.orury.domain.crew.domain.dto.CrewDto;

import java.time.LocalDateTime;

public record CrewResponse(
        Long id,
        String name,
        int memberCount,
        int capacity,
        String region,
        String description,
        String icon,
        int isDeleted,
        String headProfileImage,
        @JsonFormat(shape = JsonFormat.Shape.STRING)
        LocalDateTime createdAt,
        @JsonFormat(shape = JsonFormat.Shape.STRING)
        LocalDateTime updatedAt,
        boolean isApply
) {
    public static CrewResponse of(CrewDto crewDto, boolean isApply) {
        return new CrewResponse(
                crewDto.id(),
                crewDto.name(),
                crewDto.memberCount(),
                crewDto.capacity(),
                crewDto.region(),
                crewDto.description(),
                crewDto.icon(),
                crewDto.isDeleted(),
                crewDto.userDto().profileImage(),
                crewDto.createdAt(),
                crewDto.updatedAt(),
                isApply
        );
    }
}
