package com.example.doctor_appointment.dto;

import lombok.Data;

@Data
public class UserRegister {
    private String name;
    private Integer age;
    private String email;
    private String password;
}
