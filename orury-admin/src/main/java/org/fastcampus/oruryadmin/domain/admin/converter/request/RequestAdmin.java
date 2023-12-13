package org.fastcampus.oruryadmin.domain.admin.converter.request;

import org.fastcampus.oruryadmin.domain.admin.converter.dto.AdminDto;

public record RequestAdmin(
        String name,
        String email,
        String password
) {
    public static RequestAdmin of(
            String name,
            String email,
            String password
    ) {
        return new RequestAdmin(
                name,
                email,
                password
        );
    }

    public AdminDto toDto() {
        return AdminDto.of(
                name(),
                email(),
                password()
        );
    }
}
