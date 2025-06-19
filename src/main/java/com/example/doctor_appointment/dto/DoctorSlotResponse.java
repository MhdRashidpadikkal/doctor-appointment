package com.example.doctor_appointment.dto;

import java.time.LocalDate;
import java.time.LocalTime;

public record DoctorSlotResponse(
        Long id,
        LocalDate date,
        LocalTime startTime,
        LocalTime endTime,
        Boolean isBooked
) {}

