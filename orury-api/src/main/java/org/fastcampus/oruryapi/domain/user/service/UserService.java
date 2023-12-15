package org.fastcampus.oruryapi.domain.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.fastcampus.oruryapi.domain.user.converter.request.RequestId;
import org.fastcampus.oruryapi.domain.user.converter.request.RequestProfileImage;
import org.fastcampus.oruryapi.domain.user.converter.request.RequestUserInfo;
import org.fastcampus.oruryapi.domain.user.converter.response.ResponseMypage;
import org.fastcampus.oruryapi.domain.user.db.model.User;
import org.fastcampus.oruryapi.domain.user.db.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class UserService {
    private final UserRepository userRepository;

    public ResponseMypage readMypage(Long id){
        User user = userRepository.findUserById(id);
        return ResponseMypage.from(user);
    }

    public void updateProfileImage(RequestProfileImage requestProfileImage){
        User user = userRepository.findById(requestProfileImage.id()).orElseThrow(()-> new RuntimeException("익셉션 수정 필요"));
        user.updateProfileImage(requestProfileImage);
        userRepository.save(user);
    }

    public void updateUserInfo(RequestUserInfo requestUserInfo){
        User user = userRepository.findById(requestUserInfo.id()).orElseThrow(()-> new RuntimeException("익셉션 수정 필요"));
        user.updateUserInfo(requestUserInfo);
        userRepository.save(user);
    }


    public void deleteUser(RequestId requestId) {
        User user = userRepository.findById(requestId.id()).orElseThrow(()-> new RuntimeException("수정 필요함"));
        userRepository.delete(user);
    }
}
