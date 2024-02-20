package org.oruryclient.auth.strategy;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class LoginStrategyManager {

    private final Map<Integer, LoginStrategy> loginStrategyMap;

    public LoginStrategyManager(List<LoginStrategy> strategies) {
        loginStrategyMap = strategies.stream()
                .collect(Collectors.toMap(LoginStrategy::getSignUpType,
                        Function.identity()));
    }

    public LoginStrategy getLoginStrategy(int signUpType) {
        return loginStrategyMap.get(signUpType);
    }
}