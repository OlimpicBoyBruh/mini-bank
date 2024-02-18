package ru.sberbank.jd.controllers;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.sberbank.jd.converters.OperationConverter;
import ru.sberbank.jd.dto.DepositeAccountDto;
import ru.sberbank.jd.dto.OperationTransferDto;
import ru.sberbank.jd.exceptions.ResourceNotFoundException;
import ru.sberbank.jd.services.OperationService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/operation")
@RequiredArgsConstructor
@CrossOrigin("*")
public class OperationController {
    private final OperationService operationService;

    @PostMapping()
    public void doTransfer(@RequestBody OperationTransferDto transferDto,
                           @RequestHeader(name = "user-id") String userId) {
        operationService.doTransferOperation(transferDto, userId);
    }

    @PostMapping("/depo_close")
    public void closeDepositeAccount(@RequestBody DepositeAccountDto depositeAccountDto,
                                     @RequestHeader(name = "user-id") String userId) {

    }

    @GetMapping("/{id}")
    public OperationTransferDto findOperationById(@PathVariable Long id,
                                                  @RequestHeader(name = "user-id") String userId) {
        return OperationConverter.entityToDto(operationService.findById(id, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Операция с id " + id + " не найдена")));
    }

    @GetMapping("/history/{account}")
    public List<OperationTransferDto> findOperationByAccount(@PathVariable String account,
                                                             @RequestHeader(name = "user-id") String userId) {
        return operationService.findOperationByAccount(account, userId).stream()
                .map(OperationConverter::entityToDto)
                .collect(Collectors.toList());
    }

}
