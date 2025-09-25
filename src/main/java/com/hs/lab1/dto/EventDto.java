package com.hs.lab1.dto;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

public record EventDto(
        Long id,
        String name,
        LocalDate date,
        LocalTime startTime,
        LocalTime endTime
) {}
