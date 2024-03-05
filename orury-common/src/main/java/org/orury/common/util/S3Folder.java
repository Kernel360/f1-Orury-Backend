package org.orury.common.util;

import lombok.Getter;

@Getter
public enum S3Folder {
    POST("post"),
    REVIEW("review"),
    USER("user"),
    GYM("gym"),
    CREW("crew");

    private final String name;

    S3Folder(String name) {
        this.name = name;
    }
}
