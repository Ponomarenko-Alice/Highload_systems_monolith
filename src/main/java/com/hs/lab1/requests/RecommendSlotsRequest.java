package com.hs.lab1.requests;

import java.time.Duration;
import java.time.LocalDate;

public record RecommendSlotsRequest(
        LocalDate periodStart,
        LocalDate periodEnd,
        Duration duration,
        Long groupEventId
) {
}