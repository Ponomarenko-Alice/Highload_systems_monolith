package com.hs.lab1.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record CreateGroupEventRequest(
        @NotBlank String name,
        String description,
        @NotNull List<Long> participantIds,
        @NotNull Long ownerId
) {
}
