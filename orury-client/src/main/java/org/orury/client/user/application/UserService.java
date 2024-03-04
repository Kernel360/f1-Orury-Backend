package org.orury.client.user.application;

import org.orury.domain.user.domain.dto.UserDto;
import org.springframework.web.multipart.MultipartFile;

public interface UserService {

    UserDto getUserDtoById(Long id);

    void updateProfileImage(UserDto userDto, MultipartFile image);

    void updateUserInfo(UserDto userDto);

    void deleteUser(UserDto userDto);

}
