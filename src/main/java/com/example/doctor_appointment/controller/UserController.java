package com.example.doctor_appointment.controller;

import com.example.doctor_appointment.dto.UserResponse;
import com.example.doctor_appointment.model.User;
import com.example.doctor_appointment.repository.UserRepository;
import com.example.doctor_appointment.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/users")
@Tag(name = "User Controller", description = "Manage users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @PreAuthorize("hasRole('USER')")
    @GetMapping
    public ResponseEntity<List<UserResponse>> listAllUsers() {
        List<UserResponse> userList = userService.listAllUsers();
        return ResponseEntity.ok(userList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        User isUser = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(String.format("User not found with id %d", id)));
        return ResponseEntity.ok(isUser);
    }
}
