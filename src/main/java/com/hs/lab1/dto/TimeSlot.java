package com.hs.lab1.dto;

import java.time.LocalDate;
import java.time.LocalTime;

public record TimeSlot(LocalDate date, LocalTime start, LocalTime end) {
}
