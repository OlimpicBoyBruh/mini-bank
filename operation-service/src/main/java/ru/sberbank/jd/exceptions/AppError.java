package ru.sberbank.jd.exceptions;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Сообщения ошибок сервисов", example = "Операция не найдена")
public class AppError {

    private String status;
    private String message;

}
