package org.fastcampus.oruryclient.global.constants;

import lombok.Getter;

@Getter
public enum Constants {
    ADMIN("admin"),
    SYSTEM("system"),
    ROLE_USER("ROLE_USER"),
    ROLE_ADMIN("ROLE_ADMIN"),

    // header 종류
    AUTHORIZATION("Authorization"),
    REFRESH_HEADER("Refresh-token"),

    // default values
    DEFAULT_IMAGE("default image"),
    DEFAULT_NICKNAME("default nickname"),

    // deleted values
    DELETED_USER("deleted user")

    //

    ;

    private final String message;

    Constants(String message) {
        this.message = message;
    }

}
