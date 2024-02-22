package org.orury.client.auth.strategy;

import org.orury.client.auth.converter.request.LoginRequest;
import org.orury.domain.auth.dto.LoginDto;

public interface LoginStrategy {
    LoginDto login(LoginRequest request);

    int getSignUpType();
}
