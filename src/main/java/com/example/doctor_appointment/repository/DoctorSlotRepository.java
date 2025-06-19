package com.example.doctor_appointment.repository;

import com.example.doctor_appointment.model.DoctorProfile;
import com.example.doctor_appointment.model.DoctorSlot;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface DoctorSlotRepository extends JpaRepository<DoctorSlot, Long> {
    List<DoctorSlot> findByDoctorUserId(Long userId);

    boolean existsByDoctorAndDateAndStartTimeAndEndTime(
            DoctorProfile doctor,
            LocalDate date,
            LocalTime startTime,
            LocalTime endTime
    );
}
