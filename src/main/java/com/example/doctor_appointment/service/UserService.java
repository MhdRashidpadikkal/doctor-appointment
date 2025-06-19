package com.example.doctor_appointment.service;

import com.example.doctor_appointment.dto.DoctorRegister;
import com.example.doctor_appointment.dto.DoctorResponse;
import com.example.doctor_appointment.dto.UserRegister;
import com.example.doctor_appointment.dto.UserResponse;
import com.example.doctor_appointment.model.DoctorProfile;
import com.example.doctor_appointment.model.User;
import com.example.doctor_appointment.repository.DoctorProfileRepository;
import com.example.doctor_appointment.repository.UserRepository;
import com.example.doctor_appointment.security.CurrentUserProvider;
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

    @Autowired
    private CurrentUserProvider currentUserProvider;

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
                .filter(user -> user.getRole().name().equalsIgnoreCase("user"))
                .map(this::convertToUserResponse)
                .toList();
        return response;
    }

    public List<UserResponse> listAllAdmins() {
        List<UserResponse> response = userRepository.findAll().stream()
                .filter(user -> user.getRole().name().equalsIgnoreCase("admin"))
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

        User loggedInUser = currentUserProvider.getCurrentUser();

        // Role-based check
        if(loggedInUser.getRole().name().equalsIgnoreCase("doctor") && !loggedInUser.getId().equals(id)) {
            throw new RuntimeException("Doctors can only update their own profile");
        }

        DoctorProfile currentUser = doctorProfileRepository.findByUserId(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        currentUser.getUser().setName(dto.getName());
        currentUser.getUser().setName(dto.getName());
        currentUser.getUser().setAge(dto.getAge());
        userRepository.save(currentUser.getUser());

        currentUser.setUser(currentUser.getUser());
        currentUser.setStartTime(dto.getStartTime());
        currentUser.setEndTime(dto.getEndTime());
        currentUser.setSlotDuration(dto.getSlotDuration());
        doctorProfileRepository.save(currentUser);

        DoctorResponse updatedUser = new DoctorResponse();
        updatedUser.setId(currentUser.getUser().getId());
        updatedUser.setName(currentUser.getUser().getName());
        updatedUser.setAge(currentUser.getUser().getAge());
        updatedUser.setEmail(currentUser.getUser().getEmail());
        updatedUser.setRole(currentUser.getUser().getRole());
        updatedUser.setStartTime(dto.getStartTime());
        updatedUser.setEndTime(dto.getEndTime());
        updatedUser.setSlotDuration(dto.getSlotDuration());
        updatedUser.setCreatedDate(currentUser.getUser().getCreatedAt());
        updatedUser.setUpdatedDate(currentUser.getUser().getUpdatedAt());
        return updatedUser;
    }

    public List<DoctorResponse> listDoctors() {
        List<DoctorProfile> profiles = doctorProfileRepository.findAll();

        List<DoctorResponse> response = profiles.stream()
                .filter(profile -> profile.getUser().getRole().name().equalsIgnoreCase("doctor"))
                .map(profile -> {
                    DoctorResponse dto = new DoctorResponse();
                    dto.setId(profile.getUser().getId());
                    dto.setName(profile.getUser().getName());
                    dto.setAge(profile.getUser().getAge());
                    dto.setEmail(profile.getUser().getEmail());
                    dto.setRole(profile.getUser().getRole());
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



