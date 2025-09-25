package com.hs.lab1.dto;

import com.hs.lab1.enums.GroupEventStatus;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public record GroupEventDto (
        Long id,
        String name,
        String description,
        LocalDate date,
        LocalTime startTime,
        LocalTime endTime,
        List<UserDto> participants,
        GroupEventStatus status
)
{}
