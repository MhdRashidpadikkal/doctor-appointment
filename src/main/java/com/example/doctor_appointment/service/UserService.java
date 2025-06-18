package com.example.doctor_appointment.service;

import com.example.doctor_appointment.dto.DoctorRegister;
import com.example.doctor_appointment.dto.DoctorResponse;
import com.example.doctor_appointment.dto.UserRegister;
import com.example.doctor_appointment.dto.UserResponse;
import com.example.doctor_appointment.model.DoctorProfile;
import com.example.doctor_appointment.model.User;
import com.example.doctor_appointment.repository.DoctorProfileRepository;
import com.example.doctor_appointment.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DoctorProfileRepository doctorProfileRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole().name().toUpperCase()))
        );
    }

    public List<UserResponse> listAllUsers() {
        List<UserResponse> response = userRepository.findAll().stream()
                .map(this::convertToUserResponse)
                .toList();
        return response;
    }

    private UserResponse convertToUserResponse(User user) {
        UserResponse dto = new UserResponse();
        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setAge(user.getAge());
        dto.setEmail(user.getEmail());
        dto.setRole(user.getRole());
        dto.setCreatedAt(user.getCreatedAt());
        dto.setUpdatedAt(user.getUpdatedAt());
        return dto;
    }

    public UserResponse updateUser(UserRegister dto, Long id) {
        User currentUser = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        currentUser.setName(dto.getName());
        currentUser.setAge(dto.getAge());
        currentUser.setEmail(dto.getEmail());
        currentUser.setPassword(dto.getPassword());
        userRepository.save(currentUser);

        UserResponse updatedUser = new UserResponse();
        updatedUser.setId(currentUser.getId());
        updatedUser.setName(currentUser.getName());
        updatedUser.setAge(currentUser.getAge());
        updatedUser.setRole(currentUser.getRole());
        updatedUser.setCreatedAt(currentUser.getCreatedAt());
        updatedUser.setUpdatedAt(currentUser.getUpdatedAt());
        return updatedUser;
    }

    public DoctorResponse updateDoctor(DoctorRegister dto, Long id) {
        User currentUser = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        currentUser.setName(dto.getName());
        currentUser.setAge(dto.getAge());
        currentUser.setEmail(dto.getEmail());
        currentUser.setPassword(dto.getPassword());
        userRepository.save(currentUser);

        DoctorProfile profile = new DoctorProfile();
        profile.setUser(currentUser);
        profile.setStartTime(dto.getStartTime());
        profile.setEndTime(dto.getEndTime());
        profile.setSlotDuration(dto.getSlotDuration());
        doctorProfileRepository.save(profile);

        DoctorResponse updatedUser = new DoctorResponse();
        updatedUser.setId(currentUser.getId());
        updatedUser.setName(currentUser.getName());
        updatedUser.setAge(currentUser.getAge());
        updatedUser.setRole(currentUser.getRole());
        updatedUser.setStartTime(dto.getStartTime());
        updatedUser.setEndTime(dto.getEndTime());
        updatedUser.setSlotDuration(dto.getSlotDuration());
        updatedUser.setCreatedDate(currentUser.getCreatedAt());
        updatedUser.setUpdatedDate(currentUser.getUpdatedAt());
        return updatedUser;
    }

    public List<DoctorResponse> listDoctors() {
        List<DoctorProfile> profiles = doctorProfileRepository.findAll();

        List<DoctorResponse> response = profiles.stream()
                .map(profile -> {
                    DoctorResponse dto = new DoctorResponse();
                    dto.setId(profile.getUser().getId());
                    dto.setName(profile.getUser().getName());
                    dto.setEmail(profile.getUser().getEmail());
                    dto.setStartTime(profile.getStartTime());
                    dto.setEndTime(profile.getEndTime());
                    dto.setSlotDuration(profile.getSlotDuration());
                    dto.setCreatedDate(profile.getUser().getCreatedAt());
                    dto.setUpdatedDate(profile.getUser().getUpdatedAt());
                    return dto;
                }).toList();

        return response;
    }
}



