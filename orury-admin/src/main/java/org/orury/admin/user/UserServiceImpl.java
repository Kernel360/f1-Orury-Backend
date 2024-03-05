package org.orury.admin.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.orury.common.error.code.UserErrorCode;
import org.orury.common.error.exception.BusinessException;
import org.orury.domain.global.image.ImageReader;
import org.orury.domain.user.domain.UserReader;
import org.orury.domain.user.domain.UserStore;
import org.orury.domain.user.domain.dto.UserDto;
import org.orury.domain.user.domain.dto.UserStatus;
import org.orury.domain.user.domain.entity.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class UserServiceImpl implements UserService {
    private final UserReader userReader;
    private final UserStore userStore;
    private final ImageReader imageReader;

    /**
     * 유저 id로 유저 조회
     */
    @Override
    public UserDto getUser(Long userId) {
        var user = userReader.findUserById(userId)
                .orElseThrow(() -> new BusinessException(UserErrorCode.NOT_FOUND));
        return transferUserDto(user);
    }

    /**
     * 유저 전체 조회
     */
    @Override
    public List<UserDto> getUsers() {
        return userReader.findAll().stream()
                .map(this::transferUserDto)
                .toList();
    }

    /**
     * 유저 status를 B로 변경하여 저장 -> 삭제(제제)
     */
    @Override
    public void deleteUser(UserDto userDto) {
        var user = userDto.toEntity(UserStatus.BAN);
        userStore.save(user);
    }

    /**
     * 유저 프로필을 링크로 변환하여 DTO 반환
     */
    private UserDto transferUserDto(User user) {
        var profile = imageReader.getUserImageLink(user.getProfileImage());
        return UserDto.from(user, profile);
    }
}
