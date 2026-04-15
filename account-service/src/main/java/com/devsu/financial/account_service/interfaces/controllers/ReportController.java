package com.devsu.financial.account_service.interfaces.controllers;

import com.devsu.financial.account_service.application.queries.ReportQuery;
import com.devsu.financial.account_service.shared.utils.ResponseBuilder;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.UUID;

@RestController
@RequestMapping("/reports")
@RequiredArgsConstructor
public class ReportController {

    private final ReportQuery reportQuery;

    @GetMapping
    public ResponseEntity<?> getReport(
            @RequestParam String date,
            @RequestParam UUID client
    ) {

        String[] dates = date.split(",");

        LocalDateTime start = LocalDateTime.parse(dates[0]);
        LocalDateTime end = LocalDateTime.parse(dates[1]);

        var response = reportQuery.getReport(client, start, end);

        return ResponseEntity.ok(
                ResponseBuilder.success("Report generated successfully", response)
        );
    }
}