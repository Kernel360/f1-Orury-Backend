package org.orury.client.auth.service;

import lombok.RequiredArgsConstructor;
import org.orury.client.auth.jwt.JwtTokenProvider;
import org.orury.common.error.code.AuthErrorCode;
import org.orury.common.error.code.UserErrorCode;
import org.orury.common.error.exception.AuthException;
import org.orury.common.error.exception.BusinessException;
import org.orury.domain.auth.dto.JwtToken;
import org.orury.domain.auth.dto.SignUpDto;
import org.orury.domain.user.db.model.User;
import org.orury.domain.user.db.repository.UserRepository;
import org.orury.domain.user.dto.UserDto;
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
