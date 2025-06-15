package com.example.doctor_appointment.dto;

import lombok.Data;

import java.time.LocalTime;

@Data
public class DoctorRegister {
    private String name;
    private String email;
    private String password;
    private LocalTime startTime;
    private LocalTime endTime;
    private Integer slotDuration;
}
