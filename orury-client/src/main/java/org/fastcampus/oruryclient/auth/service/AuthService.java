package org.fastcampus.oruryclient.auth.service;

import lombok.RequiredArgsConstructor;
import org.fastcampus.oruryclient.auth.converter.request.SignInRequest;
import org.fastcampus.orurydomain.user.dto.UserDto;
import org.fastcampus.orurydomain.user.db.repository.UserRepository;
import org.fastcampus.oruryclient.global.error.BusinessException;
import org.fastcampus.oruryclient.global.error.code.UserErrorCode;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class AuthService {
    private final UserRepository userRepository;

    /**
     * 회원가입
     * @param userDto
     */
    @Transactional
    public void signUp(UserDto userDto) {
        userRepository.save(userDto.toEntity());
        try {
            userRepository.flush();
        } catch (DataIntegrityViolationException e) {
            throw new BusinessException(UserErrorCode.DUPLICATED_USER);
        }
    }

    /**
     * 로그인
     * @param request
     * @return
     */
    @Transactional
    public UserDto signIn(SignInRequest request) {
        // 카카오 토큰 전달받아서 유효한 토큰인지 확인 하는 절차 필요
        return userRepository.findByEmailAndSignUpType(request.email(), request.signUpType())
                .map(UserDto::from)
                .orElseThrow(() -> new BusinessException(UserErrorCode.NOT_FOUND))
                ;
    }
}
