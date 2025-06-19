package com.example.doctor_appointment.repository;

import com.example.doctor_appointment.model.DoctorProfile;
import com.example.doctor_appointment.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DoctorProfileRepository extends JpaRepository<DoctorProfile, Long> {

    Optional<DoctorProfile> findByUser(User user);
    Optional<DoctorProfile> findByUserId(Long userId);

}
