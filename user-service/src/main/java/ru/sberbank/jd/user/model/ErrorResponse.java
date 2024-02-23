package ru.sberbank.jd.user.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Error message in response.
 */
@RequiredArgsConstructor
public class ErrorResponse {

    @Getter
    private final String message;
}
