package org.fastcampus.oruryclient.auth.service;

import lombok.RequiredArgsConstructor;
import org.fastcampus.oruryclient.auth.converter.request.LoginRequest;
import org.fastcampus.orurycommon.error.code.UserErrorCode;
import org.fastcampus.orurycommon.error.exception.BusinessException;
import org.fastcampus.orurydomain.user.db.repository.UserRepository;
import org.fastcampus.orurydomain.user.dto.UserDto;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class AuthService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    /**
     * 회원가입
     *
     * @param userDto
     */
    @Transactional
    public void signUp(UserDto userDto) {
        try {
            userRepository.save(userDto.toEntity());
        } catch (DataIntegrityViolationException e) {
            throw new BusinessException(UserErrorCode.DUPLICATED_USER);
        }
    }

    /**
     * 로그인
     *
     * @param request
     * @return
     */
    @Transactional
    public UserDto login(LoginRequest request) {
        // 카카오 토큰 전달받아서 유효한 토큰인지 확인 하는 절차 필요
        return userRepository.findByEmailAndSignUpType(request.email(), request.signUpType())
                .map(UserDto::from)
                .orElseThrow(() -> new BusinessException(UserErrorCode.NOT_FOUND))
                ;
    }
}
