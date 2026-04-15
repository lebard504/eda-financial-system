package com.devsu.financial.customer_service.shared.utils;

import java.time.ZoneId;
import java.time.ZonedDateTime;

public class TimeZoneUtil {

    private static final String DEFAULT_TIME_ZONE = "America/Tegucigalpa";

    public static long nowInUnix(String timezone) {
        if (timezone == null || timezone.isBlank()) {
            timezone = DEFAULT_TIME_ZONE;
        }

        return ZonedDateTime.now(ZoneId.of(timezone)).toEpochSecond();
    }

    public static long nowInUnix() {
        return nowInUnix(null);
    }
}
