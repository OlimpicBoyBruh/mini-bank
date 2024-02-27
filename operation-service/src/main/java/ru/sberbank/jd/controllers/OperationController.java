package ru.sberbank.jd.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
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
            responses = {
                    @ApiResponse(
                            description = "Успешный ответ", responseCode = "200"
                    )
            }
    )
    @PostMapping()
    public void doTransfer(
            @RequestBody OperationTransferDto transferDto,
            @RequestHeader(name = "clientId") String userId,
            @AuthenticationPrincipal Jwt token) {
        operationService.doTransferOperation(transferDto, userId, token);
    }

    @PostMapping("/depo_close")
    public void closeDepositeAccount(
            @RequestBody DepositeAccountDto depositeAccountDto,
            @RequestHeader(name = "clientId") String userId,
            @AuthenticationPrincipal Jwt token) {
        operationService.closeDepositeAccount(depositeAccountDto, userId, token);

    }

    @Operation(
            summary = "Получение информации по проведенной операции",
            responses = {
                    @ApiResponse(
                            description = "Успешный ответ", responseCode = "200",
                            content = @Content(schema = @Schema(implementation = OperationTransferDto.class))
                    )
            }
    )
    @GetMapping("/{id}")
    public OperationTransferDto findOperationById(
            @PathVariable("id") @Parameter(description = "ID операции", required = true) Long id,
            @RequestHeader(name = "clientId") @Parameter(description = "ID пользователя") String userId,
            @AuthenticationPrincipal Jwt token) {
        return OperationConverter.entityToDto(operationService.findById(id, userId, token));
    }

    @Operation(
            summary = "Получение списка операций по счету",
            responses = {
                    @ApiResponse(
                            description = "Успешный ответ", responseCode = "200",
                            content = @Content(schema = @Schema(implementation = OperationTransferDto.class))
                    )
            }
    )
    @GetMapping("/history/{account}")
    public List<OperationTransferDto> findOperationByAccount(
            @PathVariable("account") @Parameter(description = "Номер счета", required = true) String account,
            @RequestHeader(name = "clientId") @Parameter(description = "ID пользователя") String userId,
            @AuthenticationPrincipal Jwt token) {
        return operationService.findOperationByAccount(account, userId, token).stream()
                .map(OperationConverter::entityToDto)
                .collect(Collectors.toList());
    }
}
