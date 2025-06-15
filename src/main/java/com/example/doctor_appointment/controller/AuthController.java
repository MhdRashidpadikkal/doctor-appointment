package com.example.doctor_appointment.controller;

import com.example.doctor_appointment.dto.*;
import com.example.doctor_appointment.service.AuthService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/auth")
@Tag(name = "Auth Controller", description = "Manage authentication")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/admin/register")
    public ResponseEntity<?> registerAdmin(@RequestBody UserRegister dto) {
        String otpSended = authService.registerAdmin(dto);
        return ResponseEntity.ok(new AuthResponse(otpSended));
    }

    @PostMapping("/users/register")
    public ResponseEntity<?> registerUser(@RequestBody UserRegister dto) {
        String token = authService.registerUser(dto);
        return ResponseEntity.ok(new AuthResponse(token));
    }

    @PostMapping("/doctors/register")
    public ResponseEntity<?> registerDoctor(@RequestBody DoctorRegister dto) {
        String token = authService.registerDoctor(dto);
        return ResponseEntity.ok(new AuthResponse(token));
    }

    @PostMapping("/verify")
    public ResponseEntity<?> verifyOtp(@RequestBody VerifyOtp dto) {
        String token = authService.verifyOtp(dto);
        return ResponseEntity.ok(token);
    }

    @PostMapping("/resend-otp")
    public ResponseEntity<?> resendOtp(@RequestBody EmailDto email) {
        String response = authService.resendOtp(email);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDto dto) {
        String token = authService.login(dto);
        return ResponseEntity.ok(new AuthResponse(token));
    }
}
