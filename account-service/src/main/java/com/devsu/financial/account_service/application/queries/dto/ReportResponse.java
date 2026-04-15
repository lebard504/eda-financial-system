package com.devsu.financial.account_service.application.queries.dto;

import lombok.*;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReportResponse {
    private UUID customerId;
    private String name;
    private String identification;
    private List<AccountReport> accounts;
}