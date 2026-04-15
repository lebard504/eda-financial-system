package com.devsu.financial.account_service.domain.repositories;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface IReportRepository {
    List<Object[]> getReport(UUID customerId, LocalDateTime start, LocalDateTime end);
}