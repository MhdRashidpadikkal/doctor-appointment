package com.example.doctor_appointment.repository;

import com.example.doctor_appointment.model.DoctorSlot;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DoctorSlotRepository extends JpaRepository<DoctorSlot, Long> {
}
