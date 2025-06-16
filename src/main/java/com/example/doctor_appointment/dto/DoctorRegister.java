package com.example.doctor_appointment.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalTime;

@Data
public class DoctorRegister {
    private String name;
    private Integer age;
    private String email;
    private String password;

    @Schema(type = "string", example = "09:30")
    private LocalTime startTime;
    @Schema(type = "string", example = "13:30")
    private LocalTime endTime;
    private Integer slotDuration;
}
