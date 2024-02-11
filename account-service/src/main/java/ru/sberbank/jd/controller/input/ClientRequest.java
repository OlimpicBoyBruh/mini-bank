package ru.sberbank.jd.controller.input;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO объект для определения клиента и его запроса.
 */
@NoArgsConstructor
@Getter
@Setter
public class ClientRequest {
    /**
     * Идентификатор клиента.
     */
    private String clientId;

}
