package com.example.doctor_appointment.controller;

import com.example.doctor_appointment.config.JwtUtil;
import com.example.doctor_appointment.dto.AuthResponse;
import com.example.doctor_appointment.dto.LoginDto;
import com.example.doctor_appointment.dto.UserRegister;
import com.example.doctor_appointment.model.Role;
import com.example.doctor_appointment.model.User;
import com.example.doctor_appointment.repository.UserRepository;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api")
@Tag(name = "User Controller", description = "Manage users")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private AuthenticationManager authManager;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/user/register")
    public ResponseEntity<?> registerUser(@RequestBody UserRegister user) {
        User newUser = new User();
        newUser.setPassword(encoder.encode(user.getPassword()));
        newUser.setName(user.getName());
        newUser.setEmail(user.getEmail());
        newUser.setRole(Role.user);

        userRepository.save(newUser);
        String token = jwtUtil.generateToken(user.getEmail());
        return ResponseEntity.ok(new AuthResponse(token));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDto request) {
        authManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        String token = jwtUtil.generateToken(request.getEmail());
        return ResponseEntity.ok(new AuthResponse(token));
    }

    @GetMapping("users/{id}")
    public ResponseEntity<?> getUserById(@RequestParam Long id) {}
}
