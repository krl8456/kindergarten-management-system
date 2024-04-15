package com.karol.kindergartenmanagementsystem.http;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ApiErrorResponse<T> {
    private ApiErrorResponseDetails<T> error;

    public static <T> ApiErrorResponse<T> of(int code, String message, T details) {
        ApiErrorResponseDetails<T> errorDetails = new ApiErrorResponseDetails<>(code, message, details);

        return new ApiErrorResponse<>(errorDetails);
    }

    @Getter
    @Setter
    @AllArgsConstructor
    private static class ApiErrorResponseDetails<T> {
        private int code;
        private String message;
        private T details;
    }
}