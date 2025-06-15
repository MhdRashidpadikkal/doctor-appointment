package com.example.doctor_appointment.dto;

import lombok.Data;

@Data
public class VerifyOtp {
    private String email;
    private String otp;
}
