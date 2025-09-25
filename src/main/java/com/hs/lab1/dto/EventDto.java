package com.hs.lab1.dto;

import java.time.LocalDate;
import java.time.LocalTime;

public record EventDto(
        Long id,
        String name,
        String description,
        LocalDate date,
        LocalTime startTime,
        LocalTime endTime,
        Long ownerId
) {}
