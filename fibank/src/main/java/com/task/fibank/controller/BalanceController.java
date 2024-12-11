package com.task.fibank.controller;


import com.task.fibank.constants.CashierConstants;
import com.task.fibank.dto.BalanceRequestDto;
import com.task.fibank.dto.ResponseDto;
import com.task.fibank.exception.CashierNotFoundException;
import com.task.fibank.service.ICashOperationsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/cash-balance")
public class BalanceController {
    private final ICashOperationsService cashOperationsService;

    @Autowired
    public BalanceController(ICashOperationsService cashOperationsService) {
        this.cashOperationsService = cashOperationsService;
    }

    @GetMapping(produces = "application/json")
    public ResponseEntity<ResponseDto> getBalance(
            @RequestParam String cashierName,
            @RequestParam(required = false) String dateFrom,
            @RequestParam(required = false) String dateTo) {

        BalanceRequestDto request = new BalanceRequestDto();
        request.setCashierName(cashierName);
        request.setDateFrom(dateFrom);
        request.setDateTo(dateTo);

        return cashOperationsService.getBalance(request)
                .map(response -> ResponseEntity.ok(new ResponseDto(CashierConstants.STATUS_200,
                        "Balance for cashier " + response.getCashierName() +
                                ": BGN: " + response.getBalanceBGN() +
                                ", EUR: " + response.getBalanceEUR())))
                .orElseThrow(() -> new CashierNotFoundException(cashierName));
    }
}
