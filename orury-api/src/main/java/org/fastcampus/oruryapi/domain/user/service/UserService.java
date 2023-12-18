package org.fastcampus.oruryapi.domain.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.fastcampus.oruryapi.domain.user.converter.dto.UserDto;
import org.fastcampus.oruryapi.domain.user.converter.request.IdRequest;
import org.fastcampus.oruryapi.domain.user.converter.request.ProfileImageRequest;
import org.fastcampus.oruryapi.domain.user.converter.request.UserInfoRequest;
import org.fastcampus.oruryapi.domain.user.converter.response.MypageResponse;
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

    @Transactional
    public MypageResponse readMypage(Long id){
        User user = userRepository.findById(id).orElseThrow(()->new BusinessException(UserErrorCode.NOT_FOUND));
        return MypageResponse.toDto(UserDto.from(user));
    }

    @Transactional
    public void updateProfileImage(ProfileImageRequest profileImageRequest){
        User user = userRepository.findById(profileImageRequest.id()).orElseThrow(()->new BusinessException(UserErrorCode.NOT_FOUND));
        userRepository.save((ProfileImageRequest.toDto(user, profileImageRequest)).toEntity());
    }

    @Transactional
    public void updateUserInfo(UserInfoRequest userInfoRequest){
        User user = userRepository.findById(userInfoRequest.id()).orElseThrow(()-> new BusinessException(UserErrorCode.NOT_FOUND));
        userRepository.save((UserInfoRequest.toDto(user, userInfoRequest)).toEntity());
    }

    @Transactional
    public void deleteUser(Long id) {
        User user = userRepository.findById(id).orElseThrow(()-> new BusinessException(UserErrorCode.NOT_FOUND));
        userRepository.delete(user);
    }
}
