package org.orury.client.crew.interfaces.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import org.apache.logging.log4j.util.Strings;
import org.hibernate.validator.constraints.Length;
import org.orury.domain.crew.domain.dto.CrewDto;
import org.orury.domain.crew.domain.dto.CrewGender;
import org.orury.domain.global.domain.Region;
import org.orury.domain.global.validation.EnumValue;
import org.orury.domain.user.domain.dto.UserDto;

import java.util.List;

public record CrewCreateRequest(
        @Length(min = 3, max = 15, message = "크루명은 3~15 글자수로 설정 가능합니다.")
        String name,

        @Min(value = 3, message = "크루 최소인원은 3명입니다.") @Max(value = 100, message = "크루 최대인원은 100명입니다.")
        int capacity,

        @EnumValue(enumClass = Region.class, message = "지역은 서울의 구 중 하나여야 합니다.")
        Region region,

        @NotEmpty(message = "크루 소개는 필수 입력사항입니다.")
        String description,

        @Min(value = 7, message = "최저 연령을 7세 이상으로 설정해 주세요.")
        int minAge,

        @Max(value = 100, message = "최고 연령을 100세 이하로 설정해 주세요.")
        int maxAge,

        @EnumValue(enumClass = CrewGender.class, message = "성별은 ANY, FEMALE, MALE 중 하나여야 합니다.")
        CrewGender gender,

        boolean permissionRequired,

        String question,

        boolean answerRequired,

        @Size(min = 1, max = 3, message = "태그는 최소 1개, 최대 3개까지만 추가할 수 있습니다.")
        List<String> tags
) {
    public static CrewCreateRequest of(
            String name,
            int capacity,
            Region region,
            String description,
            int minAge,
            int maxAge,
            CrewGender gender,
            boolean permissionRequired,
            String question,
            boolean answerRequired,
            List<String> tags
    ) {
        return new CrewCreateRequest(
                name,
                capacity,
                region,
                description,
                minAge,
                maxAge,
                gender,
                permissionRequired,
                question,
                answerRequired,
                tags
        );
    }

    public CrewDto toDto(UserDto userDto) {
        return CrewDto.of(
                null,
                name,
                0,
                capacity,
                region,
                description,
                Strings.EMPTY,
                false,
                userDto,
                null,
                null,
                minAge,
                maxAge,
                gender,
                permissionRequired,
                question,
                answerRequired,
                tags
        );
    }
}

