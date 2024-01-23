package org.fastcampus.oruryclient.gym.converter.message;

import lombok.Getter;

@Getter
public enum GymMessage {
    GYM_READ("Gym Read"),
    GYM_LIKE_CREATED("GymLike Created"),
    GYM_LIKE_DELETED("GymLike Deleted");

    private final String message;

    GymMessage(String message) {
        this.message = message;
    }
}
