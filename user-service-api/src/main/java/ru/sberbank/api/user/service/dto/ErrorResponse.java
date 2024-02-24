package ru.sberbank.api.user.service.dto;

import java.util.ArrayList;
import java.util.List;
import lombok.Data;

/**
 * Error message in response.
 */
@Data
public class ErrorResponse {

    private List<String> messages = new ArrayList<>();
}
