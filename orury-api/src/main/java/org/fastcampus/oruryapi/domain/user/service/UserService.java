package org.fastcampus.oruryapi.domain.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.fastcampus.oruryapi.domain.user.converter.dto.UserDto;
import org.fastcampus.oruryapi.domain.user.converter.response.UserResponse;
import org.fastcampus.oruryapi.domain.user.db.model.User;
import org.fastcampus.oruryapi.domain.user.db.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class UserService {
    private final UserRepository userRepository;

    public UserResponse readMypage(Long id){
        User user = userRepository.findUserById(id);
        return UserResponse.from(user);
    }
}
