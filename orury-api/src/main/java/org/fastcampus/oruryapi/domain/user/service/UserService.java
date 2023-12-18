package org.fastcampus.oruryapi.domain.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.fastcampus.oruryapi.domain.user.converter.dto.UserDto;
import org.fastcampus.oruryapi.domain.user.converter.request.RequestId;
import org.fastcampus.oruryapi.domain.user.converter.request.RequestProfileImage;
import org.fastcampus.oruryapi.domain.user.converter.request.RequestUserInfo;
import org.fastcampus.oruryapi.domain.user.converter.response.ResponseMypage;
import org.fastcampus.oruryapi.domain.user.db.model.User;
import org.fastcampus.oruryapi.domain.user.db.repository.UserRepository;
import org.fastcampus.oruryapi.global.error.BusinessException;
import org.fastcampus.oruryapi.global.error.code.UserErrorCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class UserService {
    private final UserRepository userRepository;

    public ResponseMypage readMypage(RequestId requestId){
        User user = userRepository.findById(requestId.id()).orElseThrow(()->new BusinessException(UserErrorCode.NOT_FOUND));
        return ResponseMypage.toDto(UserDto.from(user));
    }

    public void updateProfileImage(RequestProfileImage requestProfileImage){
        User user = userRepository.findById(requestProfileImage.id()).orElseThrow(()->new BusinessException(UserErrorCode.NOT_FOUND));
        userRepository.save((RequestProfileImage.toDto(user,requestProfileImage)).toEntity());
    }

    public void updateUserInfo(RequestUserInfo requestUserInfo){
        User user = userRepository.findById(requestUserInfo.id()).orElseThrow(()-> new BusinessException(UserErrorCode.NOT_FOUND));
        userRepository.save((RequestUserInfo.toDto(user,requestUserInfo)).toEntity());
    }

    public void deleteUser(RequestId requestId) {
        User user = userRepository.findById(requestId.id()).orElseThrow(()-> new BusinessException(UserErrorCode.NOT_FOUND));
        userRepository.delete(user);
    }
}
