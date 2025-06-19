package com.example.doctor_appointment.controller;

import com.example.doctor_appointment.dto.DoctorRegister;
import com.example.doctor_appointment.dto.DoctorResponse;
import com.example.doctor_appointment.dto.UserRegister;
import com.example.doctor_appointment.dto.UserResponse;
import com.example.doctor_appointment.model.User;
import com.example.doctor_appointment.repository.DoctorProfileRepository;
import com.example.doctor_appointment.repository.UserRepository;
import com.example.doctor_appointment.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/users")
@Tag(name = "User Controller", description = "Manage users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DoctorProfileRepository doctorProfileRepository;

    @Autowired
    private UserService userService;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<UserResponse>> listAllUsers() {
        List<UserResponse> userList = userService.listAllUsers();
        return ResponseEntity.ok(userList);
    }

    @PreAuthorize("hasRole('ADMIN') ")
    @GetMapping("/admins")
    public ResponseEntity<List<UserResponse>> listAllAdmins() {
        List<UserResponse> userList = userService.listAllAdmins();
        return ResponseEntity.ok(userList);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponse> updateUser(@RequestBody UserRegister dto,@PathVariable Long id) {
        UserResponse response = userService.updateUser(dto, id);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasRole('ADMIN') or hasRole('DOCTOR') ")
    @PutMapping("doctors/{id}")
    public ResponseEntity<DoctorResponse> updateDoctor(@RequestBody DoctorRegister dto, @PathVariable Long id) {
        DoctorResponse response = userService.updateDoctor(dto, id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        User isUser = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException(String.format("User not found with id %d", id)));
        return ResponseEntity.ok(isUser);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUserById(@PathVariable Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id " + id));

        doctorProfileRepository.findByUser(user).ifPresent(doctorProfileRepository::delete);

        userRepository.deleteById(id);
        return ResponseEntity.ok("User deleted successfully");
    }

    @GetMapping("/doctors")
    public ResponseEntity<List<DoctorResponse>> listDoctors() {
        List<DoctorResponse> response = userService.listDoctors();
        return ResponseEntity.ok(response);
    }
}

