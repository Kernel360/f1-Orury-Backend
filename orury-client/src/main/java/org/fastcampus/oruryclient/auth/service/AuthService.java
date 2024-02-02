package org.fastcampus.oruryclient.auth.service;

import lombok.RequiredArgsConstructor;
import org.fastcampus.oruryclient.auth.jwt.JwtTokenProvider;
import org.fastcampus.orurycommon.error.code.AuthErrorCode;
import org.fastcampus.orurycommon.error.code.UserErrorCode;
import org.fastcampus.orurycommon.error.exception.AuthException;
import org.fastcampus.orurycommon.error.exception.BusinessException;
import org.fastcampus.orurydomain.auth.dto.JwtToken;
import org.fastcampus.orurydomain.auth.dto.SignUpDto;
import org.fastcampus.orurydomain.user.db.model.User;
import org.fastcampus.orurydomain.user.db.repository.UserRepository;
import org.fastcampus.orurydomain.user.dto.UserDto;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class AuthService {
    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;

    @Transactional
    public void signUp(UserDto userDto) {
        try {
            userRepository.saveAndFlush(userDto.toEntity());
        } catch (DataIntegrityViolationException e) {
            throw new BusinessException(UserErrorCode.DUPLICATED_USER);
        }
    }

    public SignUpDto getSignUpDto(UserDto userDto) {
        User user = userRepository.findByEmail(userDto.email())
                .orElseThrow(() -> new AuthException(AuthErrorCode.NOT_EXISTING_USER_ACCOUNT));
        JwtToken jwtToken = jwtTokenProvider.issueJwtTokens(user.getId(), user.getEmail());

        return SignUpDto.of(UserDto.from(user), jwtToken);
    }
}
