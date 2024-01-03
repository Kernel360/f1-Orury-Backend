package org.fastcampus.oruryclient.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.fastcampus.orurydomain.user.dto.UserDto;
import org.fastcampus.orurydomain.user.db.model.User;
import org.fastcampus.orurydomain.user.db.repository.UserRepository;
import org.fastcampus.oruryclient.global.error.BusinessException;
import org.fastcampus.oruryclient.global.error.code.UserErrorCode;
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
