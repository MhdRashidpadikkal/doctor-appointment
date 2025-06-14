package com.example.doctor_appointment.repository;

import com.example.doctor_appointment.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
