package com.example.doctor_appointment.dto;

import com.example.doctor_appointment.model.Role;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserResponse {
    private String token;
    private Long id;
    private String name;
    private Integer age;
    private String email;
    private Role role;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
