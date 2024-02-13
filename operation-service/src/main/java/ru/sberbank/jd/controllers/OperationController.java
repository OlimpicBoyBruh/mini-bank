package ru.sberbank.jd.controllers;

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
    public void doTransfer(@RequestBody OperationTransferDto transferDto) {

    }

    @PostMapping("/depo_close")
    public void closeDepositeAccount(@RequestBody DepositeAccountDto depositeAccountDto) {

    }

    @GetMapping("/{id}")
    public OperationTransferDto findOperationById(@PathVariable Long id) {
        return OperationConverter.entityToDto(operationService.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Операция с id " + id + " не найдена")));
    }

    @GetMapping("/history/{account}")
    public List<OperationTransferDto> findOperationByAccount(@PathVariable Long account) {
        return operationService.findOperationByAccount(account).stream()
                .map(OperationConverter::entityToDto)
                .collect(Collectors.toList());
    }

}
