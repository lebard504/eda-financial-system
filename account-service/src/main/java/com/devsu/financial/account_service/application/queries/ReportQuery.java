package com.devsu.financial.account_service.application.queries;

import com.devsu.financial.account_service.domain.models.enums.TransactionType;
import com.devsu.financial.account_service.domain.repositories.IReportRepository;
import com.devsu.financial.account_service.application.queries.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class ReportQuery {

    private final IReportRepository reportRepository;

    public List<ReportItemResponse> getReport(UUID customerId, LocalDateTime start, LocalDateTime end) {
        List<Object[]> rows = reportRepository.getReport(customerId, start, end);
    
        List<ReportItemResponse> response = new ArrayList<>();
    
        for (Object[] row : rows) {
    
            // 1) Base values from DB
            BigDecimal rawAmount = row[10] != null ? (BigDecimal) row[10] : BigDecimal.ZERO;
            BigDecimal balance = row[11] != null ? (BigDecimal) row[11] : (BigDecimal) row[6];

            String typeStr = (String) row[9];

            TransactionType transactionType = null;
            if (typeStr != null) {
                transactionType = TransactionType.valueOf(typeStr.toUpperCase());
            }
    
            // 2) Compute initial balance BEFORE modifying movement
            BigDecimal initialBalance;

            if (transactionType == TransactionType.WITHDRAW) {
                initialBalance = balance.add(rawAmount);
            } 
            else {
                initialBalance = balance.subtract(rawAmount);
            }
    
            // 3) Apply business rule for movement sign
            BigDecimal movement = rawAmount;
            if (transactionType == TransactionType.WITHDRAW) {
                movement = rawAmount.negate();
            }
    
            // 4) Build response
            response.add(
                ReportItemResponse.builder()
                    .date(
                        row[8] != null
                            ? convertToLocalDateTime(row[8]).withNano(0).toString()
                            : null
                    )
                    .customer((String) row[1])
                    .accountNumber((String) row[4])
                    .type((String) row[5])
                    .initialBalance(initialBalance)
                    .status(true)
                    .movement(movement)
                    .availableBalance(balance)
                    .build()
            );
        }
    
        return response;
    }
    
    private LocalDateTime convertToLocalDateTime(Object value) {
        if (value == null) return null;
    
        if (value instanceof LocalDateTime) {
            return (LocalDateTime) value;
        }
    
        if (value instanceof java.sql.Timestamp) {
            return ((java.sql.Timestamp) value).toLocalDateTime();
        }
    
        if (value instanceof java.util.Date) {
            return new java.sql.Timestamp(((java.util.Date) value).getTime()).toLocalDateTime();
        }
    
        if (value instanceof String) {
            return LocalDateTime.parse((String) value);
        }
    
        throw new IllegalArgumentException("Unsupported date type: " + value.getClass());
    }
    
}