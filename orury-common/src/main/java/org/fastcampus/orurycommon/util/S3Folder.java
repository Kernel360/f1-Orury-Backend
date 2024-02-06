package org.fastcampus.orurycommon.util;

import lombok.Getter;

@Getter
public enum S3Folder {
    POST("post"),
    REVIEW("review"),
    USER("user"),
    GYM("gym");

    private final String name;

    S3Folder(String name) {
        this.name = name;
    }
}
