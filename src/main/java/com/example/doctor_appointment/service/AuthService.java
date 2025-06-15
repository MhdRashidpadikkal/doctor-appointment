package com.example.doctor_appointment.service;

import com.example.doctor_appointment.config.JwtUtil;
import com.example.doctor_appointment.dto.*;
import com.example.doctor_appointment.model.DoctorProfile;
import com.example.doctor_appointment.model.Role;
import com.example.doctor_appointment.model.User;
import com.example.doctor_appointment.repository.DoctorProfileRepository;
import com.example.doctor_appointment.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Random;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DoctorProfileRepository doctorProfileRepository;

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private AuthenticationManager authManager;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private EmailService emailService;

    public String registerAdmin(UserRegister dto) {
        User newUser = new User();
        newUser.setPassword(encoder.encode(dto.getPassword()));
        newUser.setName(dto.getName());
        newUser.setEmail(dto.getEmail());
        newUser.setRole(Role.admin);

        String otp = String.valueOf(new Random().nextInt(999999));
        newUser.setOtp(otp);
        newUser.setOtpExpiry(LocalDateTime.now().plusMinutes(10));

        userRepository.save(newUser);

        emailService.sendOtpEmail(dto.getEmail(), otp);
        return "Otp sended to your mail";
    }

    public String registerUser(UserRegister dto) {
        User newUser = new User();
        newUser.setPassword(encoder.encode(dto.getPassword()));
        newUser.setName(dto.getName());
        newUser.setEmail(dto.getEmail());
        newUser.setRole(Role.user);

        userRepository.save(newUser);
        return jwtUtil.generateToken(dto.getEmail());
    }

    @Transactional
    public String registerDoctor(DoctorRegister dto) {
        User newUser = new User();
        newUser.setName(dto.getName());
        newUser.setEmail(dto.getEmail());
        newUser.setPassword(encoder.encode(dto.getPassword()));
        newUser.setRole(Role.doctor);
        userRepository.save(newUser);

        DoctorProfile profile = new DoctorProfile();
        profile.setUser(newUser);
        profile.setStartTime(dto.getStartTime());
        profile.setEndTime(dto.getEndTime());
        profile.setSlotDuration(dto.getSlotDuration());
        doctorProfileRepository.save(profile);

        return jwtUtil.generateToken(newUser.getEmail());
    }

    public String verifyOtp(VerifyOtp dto) {
        User user = userRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (user.getOtp().equals(dto.getOtp()) && user.getOtpExpiry().isAfter(LocalDateTime.now())) {
            user.setEmailVerified(true);
            user.setOtp(null);
            userRepository.save(user);
            return ("Email verified");
        } else {
            return ("Invalid or expired OTP");
        }
    }

    public String resendOtp(EmailDto dto) {
        User user = userRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        String otp = String.valueOf(new Random().nextInt(999999));
        user.setOtp(otp);
        user.setOtpExpiry(LocalDateTime.now().plusMinutes(10));

        userRepository.save(user);

        emailService.sendOtpEmail(dto.getEmail(), otp);
        return "Resended otp to your mail";
    }

    public String login(LoginDto dto) {
        authManager.authenticate(new UsernamePasswordAuthenticationToken(dto.getEmail(), dto.getPassword()));
        return jwtUtil.generateToken(dto.getEmail());
    }
}
