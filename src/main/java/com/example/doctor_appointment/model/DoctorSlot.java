package com.example.doctor_appointment.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Data
@Table(
        uniqueConstraints = @UniqueConstraint(
                columnNames = {"doctor_id", "date", "start_time", "end_time"}
        )
)
public class DoctorSlot {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "doctor_id", nullable = false )
    private DoctorProfile doctor;

    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;
    private Boolean isBooked = false;
}
