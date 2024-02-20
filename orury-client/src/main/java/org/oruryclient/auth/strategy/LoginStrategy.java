package org.oruryclient.auth.strategy;

import org.oruryclient.auth.converter.request.LoginRequest;
import org.orurydomain.auth.dto.LoginDto;

public interface LoginStrategy {
    LoginDto login(LoginRequest request);

    int getSignUpType();
}
