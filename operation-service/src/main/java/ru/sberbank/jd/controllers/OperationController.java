package ru.sberbank.jd.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.sberbank.api.operation.service.dto.DepositeAccountDto;
import ru.sberbank.api.operation.service.dto.OperationTransferDto;
import ru.sberbank.jd.converters.OperationConverter;
import ru.sberbank.jd.services.OperationService;

@RestController
@RequestMapping("/operation")
@RequiredArgsConstructor
@CrossOrigin("*")
@Tag(name = "Контроллер операций по счетам", description = "Выполняет переводы и хранит историю")
public class OperationController {

    private final OperationService operationService;

    @Operation(
            summary = "Выполнение операции по переводу средств с одного счета на другой",
            responses = {@ApiResponse(description = "Успешный ответ", responseCode = "200")}
    )
    @PostMapping()
    public void doTransfer(
            @Parameter(description = "DTO операции перевода средств", name = "transferDto", required = true)
            @RequestBody OperationTransferDto transferDto,
            @Parameter(description = "ID пользователя")
            @RequestHeader(name = "user-id") String userId) {
        operationService.doTransferOperation(transferDto, userId);
    }

    @Operation(
            summary = "Закрытие депозитного счета с переводом средств и процентов на счет возврата",
            responses = {@ApiResponse(description = "Успешный ответ", responseCode = "200")}
    )
    @PostMapping("/depo_close")
    public void closeDepositeAccount(
            @Parameter(description = "DTO закрываемого депозита", name = "depositeAccountDto", required = true)
            @RequestBody DepositeAccountDto depositeAccountDto,
            @Parameter(description = "ID пользователя")
            @RequestHeader(name = "user-id") String userId) {
        operationService.closeDepositeAccount(depositeAccountDto, userId);

    }

    @Operation(
            summary = "Получение информации по проведенной операции",
            responses = {@ApiResponse(
                    description = "Успешный ответ", responseCode = "200",
                    content = @Content(schema = @Schema(implementation = OperationTransferDto.class)))}
    )
    @GetMapping("/{id}")
    public OperationTransferDto findOperationById(
            @Parameter(description = "ID операции", name = "id")
            @PathVariable(name = "id") Long id,
            @Parameter(description = "ID пользователя")
            @RequestHeader(name = "user-id") String userId) {
        return OperationConverter.entityToDto(operationService.findById(id, userId));
    }

    @Operation(
            summary = "Получение списка операций по счету",
            responses = {@ApiResponse(
                    description = "Успешный ответ", responseCode = "200",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = OperationTransferDto.class))))
            }
    )
    @GetMapping("/history/{account}")
    public List<OperationTransferDto> findOperationByAccount(
            @Parameter(description = "Номер счета")
            @PathVariable(name = "account") String account,
            @Parameter(description = "ID пользователя")
            @RequestHeader(name = "user-id") String userId) {
        return operationService.findOperationByAccount(account, userId).stream()
                .map(OperationConverter::entityToDto)
                .collect(Collectors.toList());
    }
}
