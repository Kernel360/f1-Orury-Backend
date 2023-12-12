package org.fastcampus.oruryapi.base.converter;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApiResponse<T> {
    private int code;

    private String message;

    @Valid
    private T data;

    private Error error;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Error {
        private List<String> errorMessages;
    }
}
