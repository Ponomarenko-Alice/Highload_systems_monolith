package com.hs.lab1.requests;

import java.time.Duration;
import java.time.LocalDate;
import java.util.List;

public record RecommendSlotsRequest(
        LocalDate periodStart,
        LocalDate periodEnd,
        Duration duration,
        List<Long> participantIds
) {
}