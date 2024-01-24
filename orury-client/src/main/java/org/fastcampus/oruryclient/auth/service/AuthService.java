package org.fastcampus.oruryclient.auth.service;

import lombok.RequiredArgsConstructor;
import org.fastcampus.orurycommon.error.code.UserErrorCode;
import org.fastcampus.orurycommon.error.exception.BusinessException;
import org.fastcampus.orurydomain.user.db.repository.UserRepository;
import org.fastcampus.orurydomain.user.dto.UserDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class AuthService {
    private final UserRepository userRepository;

    @Value("${spring.security.oauth2.client.registration.kakao.client-id}")
    String kakaoClientId;

    @Value("${spring.security.oauth2.client.registration.kakao.redirect-uri}")
    String kakaoRedirectURI;

    @Value("${spring.security.oauth2.client.registration.kakao.client-secret}")
    String kakaoClientSecret;

    @Transactional
    public void signUp(UserDto userDto) {
        try {
            userRepository.save(userDto.toEntity());
        } catch (DataIntegrityViolationException e) {
            throw new BusinessException(UserErrorCode.DUPLICATED_USER);
        }
    }
}
