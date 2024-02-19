package ru.sberbank.jd.user.model;

import lombok.Data;
import lombok.RequiredArgsConstructor;

/**
 * Error message in response.
 */
@Data
@RequiredArgsConstructor
public class ErrorResponse {

    private final String message;
}
