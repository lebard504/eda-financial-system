package com.devsu.financial.account_service.infrastructure.persistence.adapters;

import com.devsu.financial.account_service.domain.repositories.IReportRepository;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class ReportRepositoryImpl implements IReportRepository {

    private final EntityManager entityManager;

    @SuppressWarnings("unchecked")
    @Override
    public List<Object[]> getReport(UUID customerId, LocalDateTime start, LocalDateTime end) {

        String sql = """
            SELECT 
                cs.customer_id,
                cs.name,
                cs.identification,
                a.id,
                a.account_number,
                a.account_type,
                a.balance,
                t.id,
                t.transaction_date,
                t.transaction_type,
                t.amount,
                t.balance
            FROM accounts a
            JOIN customer_snapshots cs ON cs.customer_id = a.customer_id
            LEFT JOIN transactions t ON t.account_id = a.id
            WHERE a.customer_id = :customerId
            AND (t.transaction_date BETWEEN :start AND :end OR t.id IS NULL)
            ORDER BY a.id, t.transaction_date DESC
        """;

        return entityManager.createNativeQuery(sql)
                .setParameter("customerId", customerId)
                .setParameter("start", start)
                .setParameter("end", end)
                .getResultList();
    }
}