package com.hs.lab1.requests;

import com.hs.lab1.dto.UserDto;
import com.hs.lab1.enums.GroupEventStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public record CreateGroupEventRequest(
        @NotBlank String name,
        String description,
        @NotNull LocalDate date,
        @NotNull LocalTime startTime,
        @NotNull LocalTime endTime,
        @NotNull List<Long> participantIds,
        @NotNull Long ownerId,
        @NotNull GroupEventStatus status
) {
}
