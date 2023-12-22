package org.fastcampus.oruryapi.domain.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.fastcampus.oruryapi.domain.user.converter.dto.UserDto;
import org.fastcampus.oruryapi.domain.user.db.model.User;
import org.fastcampus.oruryapi.domain.user.db.repository.UserRepository;
import org.fastcampus.oruryapi.global.error.BusinessException;
import org.fastcampus.oruryapi.global.error.code.UserErrorCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;

    public UserDto getUserDtoById(Long id){
        User user = userRepository.findById(id).orElseThrow(()->new BusinessException(UserErrorCode.NOT_FOUND));
        return UserDto.from(user);
    }

    @Transactional
    public void updateProfileImage(UserDto userDto){
        userRepository.save(userDto.toEntity());
    }

    @Transactional
    public void updateUserInfo(UserDto userDto){
        userRepository.save(userDto.toEntity());
    }

    @Transactional
    public void deleteUser(Long id) {
        User user = userRepository.findById(id).orElseThrow(()-> new BusinessException(UserErrorCode.NOT_FOUND));
        userRepository.delete(user);
    }
}
