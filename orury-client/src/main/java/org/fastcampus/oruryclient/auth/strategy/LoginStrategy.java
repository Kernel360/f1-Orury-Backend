package org.fastcampus.oruryclient.auth.strategy;

import org.fastcampus.oruryclient.auth.converter.request.LoginRequest;
import org.fastcampus.orurydomain.auth.dto.LoginDto;

public interface LoginStrategy {
    LoginDto login(LoginRequest request);

    int getSignUpType();
}
