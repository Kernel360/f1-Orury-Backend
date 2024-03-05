package org.orury.client.crew.interfaces.response;

import com.fasterxml.jackson.annotation.JsonFormat;

import org.orury.domain.crew.domain.dto.CrewDto;
import org.orury.domain.user.domain.dto.UserDto;

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
        UserDto userDto,
        @JsonFormat(shape = JsonFormat.Shape.STRING)
        LocalDateTime createdAt,
        @JsonFormat(shape = JsonFormat.Shape.STRING)
        LocalDateTime updatedAt,
        boolean is_apply
) {
    public static CrewResponse of(CrewDto crewDto, boolean is_apply) {
        return new CrewResponse(
                crewDto.id(),
                crewDto.name(),
                crewDto.memberCount(),
                crewDto.capacity(),
                crewDto.region(),
                crewDto.description(),
                crewDto.icon(),
                crewDto.isDeleted(),
                crewDto.userDto(),
                crewDto.createdAt(),
                crewDto.updatedAt(),
                is_apply
        );
    }


}
