package ru.sberbank.jd.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.sberbank.jd.converters.OperationConverter;
import ru.sberbank.jd.dto.DepositeAccountDto;
import ru.sberbank.jd.dto.OperationTransferDto;
import ru.sberbank.jd.services.OperationService;

import java.util.List;
import java.util.stream.Collectors;

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
    public void doTransfer(@RequestBody OperationTransferDto transferDto,
                           @RequestHeader(name = "user-id") String userId) {
        operationService.doTransferOperation(transferDto, userId);
    }

    @PostMapping("/depo_close")
    public void closeDepositeAccount(@RequestBody DepositeAccountDto depositeAccountDto,
                                     @RequestHeader(name = "user-id") String userId) {
        operationService.closeDepositeAccount(depositeAccountDto, userId);

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
    public OperationTransferDto findOperationById(@PathVariable @Parameter(description = "ID операции", required = true) Long id,
                                                  @RequestHeader(name = "user-id") @Parameter(description = "ID пользователя") String userId) {
        return OperationConverter.entityToDto(operationService.findById(id, userId));
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
    public List<OperationTransferDto> findOperationByAccount(@PathVariable @Parameter(description = "Номер счета", required = true) String account,
                                                             @RequestHeader(name = "user-id") @Parameter(description = "ID пользователя") String userId) {
        return operationService.findOperationByAccount(account, userId).stream()
                .map(OperationConverter::entityToDto)
                .collect(Collectors.toList());
    }
}
