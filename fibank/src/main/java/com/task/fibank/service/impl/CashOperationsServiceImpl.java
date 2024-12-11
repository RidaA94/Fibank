package com.task.fibank.service.impl;


import com.task.fibank.dto.BalanceRequestDto;
import com.task.fibank.dto.BalanceResponseDto;
import com.task.fibank.dto.TransactionRequestDto;
import com.task.fibank.entity.Cashier;
import com.task.fibank.entity.Transaction;
import com.task.fibank.exception.CashierNotFoundException;
import com.task.fibank.repository.CashierRepository;
import com.task.fibank.repository.TransactionRepository;
import com.task.fibank.service.ICashOperationsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Service
public class CashOperationsServiceImpl implements ICashOperationsService {
    private static final Logger logger = LoggerFactory.getLogger(CashOperationsServiceImpl.class);
    private final CashierRepository cashierRepository;
    private final TransactionRepository transactionRepository;

    @Autowired
    public CashOperationsServiceImpl(CashierRepository cashierRepository, TransactionRepository transactionRepository) {
        this.cashierRepository = cashierRepository;
        this.transactionRepository = transactionRepository;
        initializeCashiers(); // Method to initialize cashiers if necessary
    }

    private void initializeCashiers() {
        createCashierIfNotExists("MARTINA", 1000, 2000, "50x10, 10x50", "100x10, 20x50");
        createCashierIfNotExists("PETER", 1000, 2000, "50x10, 10x50", "100x10, 20x50");
        createCashierIfNotExists("LINDA", 1000, 2000, "50x10, 10x50", "100x10, 20x50");
    }

    private void createCashierIfNotExists(String name, double balanceBGN, double balanceEUR, String denominationsBGN, String denominationsEUR) {
        if (cashierRepository.findByName(name) == null) {
            cashierRepository.save(new Cashier(name, balanceBGN, balanceEUR, denominationsBGN, denominationsEUR));
        }
    }

    @Override
    public Optional<BalanceResponseDto> deposit(TransactionRequestDto request) {
        logger.info("Processing deposit for cashier: {}", request.getCashierName());

        Cashier cashier = Optional.ofNullable(cashierRepository.findByName(request.getCashierName()))
                .orElseThrow(() -> new CashierNotFoundException(request.getCashierName()));

        // Update the cashier's balance
        cashier.setBalanceBGN(cashier.getBalanceBGN() + request.getAmount());
        cashierRepository.save(cashier); // Persist the updated balance

        // Log transaction and balance
        logTransactionToFile(cashier.getName(), "Deposit", request.getAmount());
        logBalanceToFile(cashier);

        // Prepare BalanceResponseDto
        List<String> denominationsBGN = List.of(cashier.getDenominationsBGN().split(","));
        List<String> denominationsEUR = List.of(cashier.getDenominationsEUR().split(","));

        return Optional.of(new BalanceResponseDto(cashier.getName(),
                cashier.getBalanceBGN(),
                cashier.getBalanceEUR(),
                denominationsBGN,
                denominationsEUR));
    }

    @Override
    public Optional<BalanceResponseDto> depositEUR(TransactionRequestDto request) {
        // Implementing logic to deposit in EUR
        Cashier cashier = getCashierByName(request.getCashierName());

        cashier.setBalanceEUR(cashier.getBalanceEUR() + request.getAmount());
        cashierRepository.save(cashier); // Persist the updated cashier

        logTransactionToFile(cashier.getName(), "DepositEUR", request.getAmount());
        logBalanceToFile(cashier);

        return Optional.of(new BalanceResponseDto(cashier.getName(),
                cashier.getBalanceBGN(),
                cashier.getBalanceEUR(),
                List.of(cashier.getDenominationsBGN().split(",")),
                List.of(cashier.getDenominationsEUR().split(","))));
    }

    @Override
    public Optional<BalanceResponseDto> withdraw(TransactionRequestDto request) {
        logger.info("Processing withdrawal for cashier: {}", request.getCashierName());

        Cashier cashier = Optional.ofNullable(cashierRepository.findByName(request.getCashierName()))
                .orElseThrow(() -> new CashierNotFoundException(request.getCashierName()));

        if (request.getAmount() > cashier.getBalanceBGN()) {
            logger.error("Insufficient funds for withdrawal. Cashier: {}, Requested Amount: {}, Available Balance: {}",
                    request.getCashierName(), request.getAmount(), cashier.getBalanceBGN());
            throw new IllegalArgumentException("Insufficient funds for withdrawal.");
        }

        // Update the cashier's balance
        cashier.setBalanceBGN(cashier.getBalanceBGN() - request.getAmount());
        cashierRepository.save(cashier); // Persist the updated balance

        // Log transaction and balance
        logTransactionToFile(cashier.getName(), "Withdrawal", request.getAmount());
        logBalanceToFile(cashier);

        // Prepare BalanceResponseDto
        List<String> denominationsBGN = List.of(cashier.getDenominationsBGN().split(","));
        List<String> denominationsEUR = List.of(cashier.getDenominationsEUR().split(","));

        return Optional.of(new BalanceResponseDto(cashier.getName(),
                cashier.getBalanceBGN(),
                cashier.getBalanceEUR(),
                denominationsBGN,
                denominationsEUR));
    }

    @Override
    public Optional<BalanceResponseDto> withdrawEUR(TransactionRequestDto request) {
        // Implementing logic to withdraw in EUR
        Cashier cashier = getCashierByName(request.getCashierName());

        if (request.getAmount() > cashier.getBalanceEUR()) {
            throw new IllegalArgumentException("Insufficient funds for EUR withdrawal.");
        }

        cashier.setBalanceEUR(cashier.getBalanceEUR() - request.getAmount());
        cashierRepository.save(cashier); // Persist the updated cashier

        logTransactionToFile(cashier.getName(), "WithdrawalEUR", request.getAmount());
        logBalanceToFile(cashier);

        return Optional.of(new BalanceResponseDto(cashier.getName(),
                cashier.getBalanceBGN(),
                cashier.getBalanceEUR(),
                List.of(cashier.getDenominationsBGN().split(",")),
                List.of(cashier.getDenominationsEUR().split(","))));
    }

    @Override
    public Optional<BalanceResponseDto> getBalance(BalanceRequestDto request) {
        logger.info("Fetching balance for cashier: {}", request.getCashierName());

        Cashier cashier = Optional.ofNullable(cashierRepository.findByName(request.getCashierName()))
                .orElseThrow(() -> new CashierNotFoundException(request.getCashierName()));

        // Initialize balances
        double balanceBGN = cashier.getBalanceBGN();
        double balanceEUR = cashier.getBalanceEUR();

        // Process date range if provided
        if (request.getDateFrom() != null && request.getDateTo() != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            LocalDateTime startDate = LocalDateTime.parse(request.getDateFrom(), formatter);
            LocalDateTime endDate = LocalDateTime.parse(request.getDateTo(), formatter);

            logger.info("Date range provided: From {} to {}", request.getDateFrom(), request.getDateTo());

            // Fetch transactions within the specified date range
            List<Transaction> transactions = transactionRepository.findByCashierIdAndTimestampBetween(cashier.getId(), startDate, endDate);

            // Aggregate balances based on transactions
            for (Transaction transaction : transactions) {
                switch (transaction.getType()) {
                    case "Deposit":
                        balanceBGN += transaction.getAmount();
                        break;
                    case "Withdrawal":
                        balanceBGN -= transaction.getAmount();
                        break;
                    case "DepositEUR":
                        balanceEUR += transaction.getAmount();
                        break;
                    case "WithdrawalEUR":
                        balanceEUR -= transaction.getAmount();
                        break;
                }
            }
        }

        // Preparing the BalanceResponseDto with potentially updated balances
        List<String> denominationsBGN = List.of(cashier.getDenominationsBGN().split(","));
        List<String> denominationsEUR = List.of(cashier.getDenominationsEUR().split(","));

        logger.info("Returning balance for cashier {}: BGN: {}, EUR: {}", cashier.getName(), balanceBGN, balanceEUR);
        return Optional.of(new BalanceResponseDto(cashier.getName(),
                balanceBGN,
                balanceEUR,
                denominationsBGN,
                denominationsEUR));
    }

    @Override
    public Optional<List<Transaction>> getTransactionHistory(String cashierName) {
        // Log the action
        logger.info("Fetching transaction history for cashier: {}", cashierName);

        // Find the cashier by name
        Cashier cashier = Optional.ofNullable(cashierRepository.findByName(cashierName))
                .orElseThrow(() -> {
                    logger.warn("Cashier not found for transaction history: {}", cashierName);
                    return new CashierNotFoundException(cashierName);
                });

        // Fetch transactions for the found cashier
        List<Transaction> transactions = transactionRepository.findByCashierId(cashier.getId());

        // Log the number of transactions found
        logger.info("Found {} transactions for cashier: {}", transactions.size(), cashierName);

        // Return the transactions wrapped in Optional
        return Optional.of(transactions);
    }

    private void logTransactionToFile(String cashierName, String type, double amount) {
        // Define a date format: DD/MM/YYYY HH:mm
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        String timestamp = LocalDateTime.now().format(formatter); // Current time formatted

        // Format amount to two decimal places
        String formattedAmount = String.format("%.2f", amount).replace('.', ',');

        String record = String.format("%s|%s|%s|%s%n",
                cashierName, type, formattedAmount, timestamp);
        writeToFile("src/main/resources/transactions.txt", record);
        logger.info("Transaction logged: {} - Amount: {} for cashier {}", type, formattedAmount, cashierName);
    }

    private void logBalanceToFile(Cashier cashier) {
        String record = String.format("CashierName: %s\nBalanceBGN: %.2f\nBalanceEUR: %.2f\nDenominationsBGN: %s\nDenominationsEUR: %s\n\n",
                cashier.getName(),
                cashier.getBalanceBGN(),
                cashier.getBalanceEUR(),
                cashier.getDenominationsBGN(),
                cashier.getDenominationsEUR());
        writeToFile("src/main/resources/balances.txt", record);
        logger.info("Balance logged for cashier: {}", cashier.getName());
    }

    private void writeToFile(String filePath, String content) {
        try {
            Files.write(Paths.get(filePath),
                    content.getBytes(),
                    StandardOpenOption.APPEND,
                    StandardOpenOption.CREATE);
        } catch (Exception e) {
            logger.error("Error writing to file: ", e);
        }
    }

    private Cashier getCashierByName(String cashierName) {
        return Optional.ofNullable(cashierRepository.findByName(cashierName))
                .orElseThrow(() -> new CashierNotFoundException(cashierName));
    }
}
