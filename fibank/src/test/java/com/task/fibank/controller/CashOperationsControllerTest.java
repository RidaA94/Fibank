package com.task.fibank.controller;

import com.task.fibank.dto.BalanceResponseDto;
import com.task.fibank.dto.TransactionRequestDto;
import com.task.fibank.exception.CashierNotFoundException;
import com.task.fibank.service.ICashOperationsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CashOperationsController.class)
public class CashOperationsControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ICashOperationsService cashOperationsService;

    @InjectMocks
    private CashOperationsController cashOperationsController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testDepositSuccess() throws Exception {
        // Create a request DTO
        TransactionRequestDto request = new TransactionRequestDto();
        request.setCashierName("MARTINA");
        request.setType("Deposit");
        request.setAmount(100.00);

        // Mock service behavior
        BalanceResponseDto responseDto = new BalanceResponseDto("MARTINA", 1100.00, 2000.00, List.of("50x10"), List.of("100x10"));
        when(cashOperationsService.deposit(request)).thenReturn(Optional.of(responseDto));

        // Execute the request
        mockMvc.perform(post("/api/v1/cash-operation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"cashierName\":\"MARTINA\",\"type\":\"Deposit\",\"amount\":100.00}")
                        .header("FIB-X-AUTH", "f9Uie8nNf112hx8s"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.statusCode").value(201))
                .andExpect(jsonPath("$.statusMessage").value("Deposit successful. New balance: 1100.00"));
    }

    @Test
    void testWithdrawalSuccess() throws Exception {
        // Create a request DTO
        TransactionRequestDto request = new TransactionRequestDto();
        request.setCashierName("MARTINA");
        request.setType("Withdrawal");
        request.setAmount(50.00);

        // Mock service behavior
        BalanceResponseDto responseDto = new BalanceResponseDto("MARTINA", 950.00, 2000.00, List.of("50x10"), List.of("100x10"));
        when(cashOperationsService.withdraw(request)).thenReturn(Optional.of(responseDto));

        // Execute the request
        mockMvc.perform(post("/api/v1/cash-operation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"cashierName\":\"MARTINA\",\"type\":\"Withdrawal\",\"amount\":50.00}")
                        .header("FIB-X-AUTH", "f9Uie8nNf112hx8s"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statusCode").value(200))
                .andExpect(jsonPath("$.statusMessage").value("Withdrawal successful. Remaining balance: 950.00"));
    }

    @Test
    void testInvalidTransactionType() throws Exception {
        // Create a request DTO
        TransactionRequestDto request = new TransactionRequestDto();
        request.setCashierName("MARTINA");
        request.setType("InvalidType");
        request.setAmount(100.00);

        // Execute the request
        mockMvc.perform(post("/api/v1/cash-operation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"cashierName\":\"MARTINA\",\"type\":\"InvalidType\",\"amount\":100.00}")
                        .header("FIB-X-AUTH", "f9Uie8nNf112hx8s"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Invalid Transaction Type"))
                .andExpect(jsonPath("$.message").value("Use 'Deposit' or 'Withdrawal'"));
    }

    @Test
    void testCashierNotFoundException() throws Exception {
        // Create a request DTO
        TransactionRequestDto request = new TransactionRequestDto();
        request.setCashierName("UNKNOWN");
        request.setType("Withdrawal");
        request.setAmount(50.00);

        // Mock service to throw CashierNotFoundException
        when(cashOperationsService.withdraw(request)).thenThrow(new CashierNotFoundException("UNKNOWN"));

        // Execute the request
        mockMvc.perform(post("/api/v1/cash-operation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"cashierName\":\"UNKNOWN\",\"type\":\"Withdrawal\",\"amount\":50.00}")
                        .header("FIB-X-AUTH", "f9Uie8nNf112hx8s"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Cashier Not Found"))
                .andExpect(jsonPath("$.message").value("Cashier with name UNKNOWN not found."));
    }
}
