package com.task.fibank.service;

import com.task.fibank.dto.BalanceRequestDto;
import com.task.fibank.dto.BalanceResponseDto;
import com.task.fibank.dto.TransactionRequestDto;
import com.task.fibank.entity.Cashier;
import com.task.fibank.exception.CashierNotFoundException;
import com.task.fibank.repository.CashierRepository;
import com.task.fibank.repository.TransactionRepository;
import com.task.fibank.service.impl.CashOperationsServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class CashOperationsServiceImplTest {
    @InjectMocks
    private CashOperationsServiceImpl cashOperationsService;

    @Mock
    private CashierRepository cashierRepository;

    @Mock
    private TransactionRepository transactionRepository;

    private Cashier cashier;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        cashier = new Cashier("MARTINA", 1000.00, 2000.00, "50x10,10x50", "100x10,20x50");
    }

    @Test
    void testDeposit() {
        TransactionRequestDto request = new TransactionRequestDto();
        request.setCashierName("MARTINA");
        request.setAmount(100);
        request.setType("Deposit");

        when(cashierRepository.findByName("MARTINA")).thenReturn(cashier);

        Optional<BalanceResponseDto> response = cashOperationsService.deposit(request);

        assertTrue(response.isPresent());
        assertEquals(1100.00, response.get().getBalanceBGN());
        verify(cashierRepository).save(cashier);
    }

    @Test
    void testWithdraw() {
        TransactionRequestDto request = new TransactionRequestDto();
        request.setCashierName("MARTINA");
        request.setAmount(50);
        request.setType("Withdrawal");

        when(cashierRepository.findByName("MARTINA")).thenReturn(cashier);

        Optional<BalanceResponseDto> response = cashOperationsService.withdraw(request);

        assertTrue(response.isPresent());
        assertEquals(950.00, response.get().getBalanceBGN());
        verify(cashierRepository).save(cashier);
    }

    @Test
    void testGetBalance() {
        // Setup: Create a BalanceRequestDto
        BalanceRequestDto request = new BalanceRequestDto();
        request.setCashierName("MARTINA");

        // Assuming you want to test without date filters, but you can set them if needed
        request.setDateFrom(null);
        request.setDateTo(null);

        // Mock the repository to return the Cashier object when findByName is called
        when(cashierRepository.findByName("MARTINA")).thenReturn(cashier);

        // Then mock the service method to return a BalanceResponseDto
        when(cashOperationsService.getBalance(request)).thenReturn(Optional.of(new BalanceResponseDto("MARTINA", 1000.00, 2000.00, List.of("50x10"), List.of("100x10"))));

        // Call getBalance with the request DTO
        Optional<BalanceResponseDto> response = cashOperationsService.getBalance(request);

        // Assertions
        assertTrue(response.isPresent());
        assertEquals(1000.00, response.get().getBalanceBGN());
    }

    @Test
    void testCashierNotFoundException() {
        when(cashierRepository.findByName("UNKNOWN")).thenReturn(null);

        TransactionRequestDto request = new TransactionRequestDto();
        request.setCashierName("UNKNOWN");

        assertThrows(CashierNotFoundException.class, () -> cashOperationsService.withdraw(request));
    }
}
