package org.orury.admin.user;

import org.orury.domain.user.domain.dto.UserDto;

import java.util.List;

public interface UserService {
    UserDto getUser(Long userId);

    List<UserDto> getUsers();

    void deleteUser(UserDto userDto);
}
