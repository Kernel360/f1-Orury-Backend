package org.fastcampus.oruryapi.domain.auth.service;

import lombok.RequiredArgsConstructor;
import org.fastcampus.oruryapi.domain.auth.converter.request.SignInRequest;
import org.fastcampus.oruryapi.domain.user.converter.dto.UserDto;
import org.fastcampus.oruryapi.domain.user.db.model.User;
import org.fastcampus.oruryapi.domain.user.db.repository.UserRepository;
import org.fastcampus.oruryapi.global.message.error.ErrorMessage;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class AuthService {
    private final UserRepository userRepository;

    @Transactional
    public UserDto signIn(SignInRequest request) {
        return userRepository.findByEmailAndSignUpType(request.email(), request.signUpType())
                .map(UserDto::from)
                .orElseThrow(() -> new IllegalArgumentException(ErrorMessage.USER_INVALID_ACCOUNT.toString()))
                ;
    }
}
