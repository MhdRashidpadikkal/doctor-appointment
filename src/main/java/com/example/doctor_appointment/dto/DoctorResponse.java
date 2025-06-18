package com.example.doctor_appointment.dto;

import com.example.doctor_appointment.model.Role;

import lombok.Data;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
public class DoctorResponse {
    private String token;
    private Long id;
    private String name;
    private Integer age;
    private String email;
    private Role role;
    private LocalTime startTime;
    private LocalTime endTime;
    private Integer slotDuration;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
}
