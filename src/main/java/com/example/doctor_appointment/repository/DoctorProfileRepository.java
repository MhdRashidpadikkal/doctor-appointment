package com.example.doctor_appointment.repository;

import com.example.doctor_appointment.model.DoctorProfile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DoctorProfileRepository extends JpaRepository<DoctorProfile, Long> {
}
