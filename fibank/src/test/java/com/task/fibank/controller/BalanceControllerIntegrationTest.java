package com.task.fibank.controller;

import com.task.fibank.dto.BalanceRequestDto;
import com.task.fibank.dto.BalanceResponseDto;
import com.task.fibank.exception.CashierNotFoundException;
import com.task.fibank.service.ICashOperationsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BalanceController.class)
public class BalanceControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ICashOperationsService cashOperationsService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetBalanceSuccess() throws Exception {
        // Create a BalanceResponseDto object for the mock response
        BalanceResponseDto responseDto = new BalanceResponseDto("MARTINA", 1000.00, 2000.00, List.of("50x10"), List.of("100x10"));

        // Mock the service method to return the balance response
        when(cashOperationsService.getBalance(any(BalanceRequestDto.class))).thenReturn(Optional.of(responseDto));

        // Perform the GET request
        mockMvc.perform(get("/api/v1/cash-balance")
                        .param("cashierName", "MARTINA")
                        .header("FIB-X-AUTH", "f9Uie8nNf112hx8s")) // Include API key in the header
                .andExpect(status().isOk()) // Expect HTTP 200 OK
                .andExpect(content().contentType("application/json")) // Expect JSON response
                .andExpect(jsonPath("$.statusCode").value(200)) // Check status code
                .andExpect(jsonPath("$.statusMessage").value("Balance for cashier MARTINA: BGN: 1000.00, EUR: 2000.00")); // Check expected message
    }

    @Test
    void testGetBalanceCashierNotFound() throws Exception {
        // Mock the service to throw CashierNotFoundException when an unknown cashier is requested
        when(cashOperationsService.getBalance(any(BalanceRequestDto.class)))
                .thenThrow(new CashierNotFoundException("UNKNOWN"));

        // Perform the GET request
        mockMvc.perform(get("/api/v1/cash-balance")
                        .param("cashierName", "UNKNOWN")
                        .header("FIB-X-AUTH", "f9Uie8nNf112hx8s")) // Include API key
                .andExpect(status().isNotFound()) // Expect HTTP 404 Not Found
                .andExpect(content().contentType("application/json")) // Expect JSON response
                .andExpect(jsonPath("$.error").value("Cashier Not Found")) // Check error message
                .andExpect(jsonPath("$.message").value("Cashier with name UNKNOWN not found.")); // Check detailed message
    }
}
