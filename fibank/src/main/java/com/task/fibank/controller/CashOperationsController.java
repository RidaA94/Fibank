package com.task.fibank.controller;


import com.task.fibank.constants.CashierConstants;
import com.task.fibank.dto.ErrorResponseDto;
import com.task.fibank.dto.ResponseDto;
import com.task.fibank.dto.TransactionRequestDto;
import com.task.fibank.exception.CashierNotFoundException;
import com.task.fibank.service.ICashOperationsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/cash-operation")
public class CashOperationsController {
    private final ICashOperationsService cashOperationsService;

    @Autowired
    public CashOperationsController(ICashOperationsService cashOperationsService) {
        this.cashOperationsService = cashOperationsService;
    }

    @PostMapping(produces = "application/json")
    public ResponseEntity<ResponseDto> handleTransaction(@RequestBody TransactionRequestDto request) {
        if ("Deposit".equalsIgnoreCase(request.getType())) {
            return cashOperationsService.deposit(request)
                    .map(balance -> ResponseEntity.status(HttpStatus.CREATED)
                            .body(new ResponseDto(CashierConstants.STATUS_201,
                                    "Deposit successful. New balance: " + balance.getBalanceBGN())))
                    .orElseThrow(() -> new CashierNotFoundException(request.getCashierName()));
        } else if ("Withdrawal".equalsIgnoreCase(request.getType())) {
            return cashOperationsService.withdraw(request)
                    .map(balance -> ResponseEntity.ok(new ResponseDto(CashierConstants.STATUS_200,
                            "Withdrawal successful. Remaining balance: " + balance.getBalanceBGN())))
                    .orElseThrow(() -> new CashierNotFoundException(request.getCashierName()));
        } else if ("DepositEUR".equalsIgnoreCase(request.getType())) {
            // Handle EUR deposit logic
            return cashOperationsService.depositEUR(request)  // New method for EUR deposits
                    .map(balance -> ResponseEntity.status(HttpStatus.CREATED)
                            .body(new ResponseDto(CashierConstants.STATUS_201,
                                    "EUR Deposit successful. New balance: " + balance.getBalanceEUR())))
                    .orElseThrow(() -> new CashierNotFoundException(request.getCashierName()));
        } else if ("WithdrawalEUR".equalsIgnoreCase(request.getType())) {
            // Handle EUR withdrawal logic
            return cashOperationsService.withdrawEUR(request)  // New method for EUR withdrawals
                    .map(balance -> ResponseEntity.ok(new ResponseDto(CashierConstants.STATUS_200,
                            "EUR Withdrawal successful. Remaining balance: " + balance.getBalanceEUR())))
                    .orElseThrow(() -> new CashierNotFoundException(request.getCashierName()));
        } else {
            return ResponseEntity.badRequest()
                    .body(new ResponseDto(CashierConstants.STATUS_400,
                            CashierConstants.MESSAGE_400));
        }
    }
}
