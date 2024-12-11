package com.task.fibank.service;

import com.task.fibank.dto.BalanceRequestDto;
import com.task.fibank.dto.BalanceResponseDto;
import com.task.fibank.dto.TransactionRequestDto;
import com.task.fibank.entity.Transaction;

import java.util.List;
import java.util.Optional;

public interface ICashOperationsService {
    Optional<BalanceResponseDto> deposit(TransactionRequestDto request);
    Optional<BalanceResponseDto> withdraw(TransactionRequestDto request);
    Optional<BalanceResponseDto> depositEUR(TransactionRequestDto request);
    Optional<BalanceResponseDto> withdrawEUR(TransactionRequestDto request);
    Optional<BalanceResponseDto> getBalance(BalanceRequestDto request);
    Optional<List<Transaction>> getTransactionHistory(String cashierName);
}
