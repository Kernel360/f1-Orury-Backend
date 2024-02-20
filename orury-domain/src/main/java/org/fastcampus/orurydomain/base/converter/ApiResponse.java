package org.fastcampus.orurydomain.base.converter;


import org.springframework.http.HttpStatus;

public record ApiResponse(
        int status,
        String message,
        Object data
) {
    //기존의 ApiResponse<Object>.builder
    public static ApiResponse of(String message) {
        return ApiResponse.of(
                message,
                null
        );
    }

    //기존의 ApiResponse<CustomResponse>.builder
    public static ApiResponse of(String message, Object data) {
        return ApiResponse.of(
                HttpStatus.OK.value(),
                message,
                data
        );
    }

    public static ApiResponse of(int status, String message, Object data) {
        return new ApiResponse(
                status,
                message,
                data
        );
    }
}
