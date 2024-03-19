package org.orury.domain;

import org.orury.domain.crew.domain.dto.CrewDto;
import org.orury.domain.crew.domain.dto.CrewGender;
import org.orury.domain.crew.domain.dto.CrewStatus;
import org.orury.domain.global.domain.Region;
import org.orury.domain.user.domain.dto.UserDto;
import org.orury.domain.user.domain.dto.UserStatus;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;


public class DomainFixtureFactory {

    private DomainFixtureFactory() {
        throw new IllegalStateException("Utility class");
    }

    private static List<String> extractFields(List<Object> objects) {
        return objects.stream()
                .filter(o -> objects.indexOf(o) % 2 == 0)
                .map(String::valueOf)
                .toList();
    }

    private static List<Object> extractValues(List<Object> objects) {
        return objects.stream()
                .filter(o -> objects.indexOf(o) % 2 == 1)
                .toList();
    }

    private static void validateFields(List<String> fields, List<String> objectsFields) {
        if (!fields.containsAll(objectsFields))
            throw new IllegalArgumentException("Invalid field name in : " + objectsFields);
    }

    private static <T> T extractFieldValue(List<String> fields, List<Object> values, String fieldName, T defaultValue) {
        int index = fields.indexOf(fieldName);
        return index != -1 ? (T) values.get(index) : defaultValue;
    }

    private static final List<String> CREW_DTO_FIELD_NAMES = Arrays.stream(CrewDto.class.getDeclaredFields()).map(Field::getName).toList();

    public static CrewDto createCrewDto(List<Object> objects) {
        List<String> fields = extractFields(objects);
        List<Object> values = extractValues(objects);

        validateFields(CREW_DTO_FIELD_NAMES, fields);

        return CrewDto.of(
                extractFieldValue(fields, values, CREW_DTO_FIELD_NAMES.get(0), 333L),
                extractFieldValue(fields, values, CREW_DTO_FIELD_NAMES.get(1), "테스트크루"),
                extractFieldValue(fields, values, CREW_DTO_FIELD_NAMES.get(2), 12),
                extractFieldValue(fields, values, CREW_DTO_FIELD_NAMES.get(3), 30),
                extractFieldValue(fields, values, CREW_DTO_FIELD_NAMES.get(4), Region.강남구),
                extractFieldValue(fields, values, CREW_DTO_FIELD_NAMES.get(5), "크루 설명"),
                extractFieldValue(fields, values, CREW_DTO_FIELD_NAMES.get(6), "orury/crew/crew_icon"),
                extractFieldValue(fields, values, CREW_DTO_FIELD_NAMES.get(7), CrewStatus.ACTIVATED),
                extractFieldValue(fields, values, CREW_DTO_FIELD_NAMES.get(8), createUserDto(Collections.emptyList())),
                LocalDateTime.now(),
                LocalDateTime.now(),
                extractFieldValue(fields, values, CREW_DTO_FIELD_NAMES.get(11), 15),
                extractFieldValue(fields, values, CREW_DTO_FIELD_NAMES.get(12), 35),
                extractFieldValue(fields, values, CREW_DTO_FIELD_NAMES.get(13), CrewGender.ANY),
                extractFieldValue(fields, values, CREW_DTO_FIELD_NAMES.get(14), false),
                extractFieldValue(fields, values, CREW_DTO_FIELD_NAMES.get(15), null),
                extractFieldValue(fields, values, CREW_DTO_FIELD_NAMES.get(16), false),
                extractFieldValue(fields, values, CREW_DTO_FIELD_NAMES.get(17), List.of("크루태그1", "크루태그2"))
        );
    }

    private static final List<String> USER_DTO_FIELD_NAMES = Arrays.stream(UserDto.class.getDeclaredFields()).map(Field::getName).toList();

    public static UserDto createUserDto(List<Object> objects) {
        List<String> fields = extractFields(objects);
        List<Object> values = extractValues(objects);

        validateFields(USER_DTO_FIELD_NAMES, fields);

        return UserDto.of(
                extractFieldValue(fields, values, USER_DTO_FIELD_NAMES.get(0), 111L),
                extractFieldValue(fields, values, USER_DTO_FIELD_NAMES.get(1), "userEmail"),
                extractFieldValue(fields, values, USER_DTO_FIELD_NAMES.get(2), "userNickname"),
                extractFieldValue(fields, values, USER_DTO_FIELD_NAMES.get(3), "userPassword"),
                extractFieldValue(fields, values, USER_DTO_FIELD_NAMES.get(4), 1),
                extractFieldValue(fields, values, USER_DTO_FIELD_NAMES.get(5), 1),
                extractFieldValue(fields, values, USER_DTO_FIELD_NAMES.get(6), LocalDate.now().minusYears(25)),
                extractFieldValue(fields, values, USER_DTO_FIELD_NAMES.get(7), "userProfileImage"),
                LocalDateTime.now(),
                LocalDateTime.now(),
                extractFieldValue(fields, values, USER_DTO_FIELD_NAMES.get(10), UserStatus.ENABLE)
        );
    }
}
