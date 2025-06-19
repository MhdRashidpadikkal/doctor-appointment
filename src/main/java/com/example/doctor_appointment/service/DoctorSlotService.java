package com.example.doctor_appointment.service;

import com.example.doctor_appointment.dto.DoctorSlotResponse;
import com.example.doctor_appointment.dto.SlotResponse;
import com.example.doctor_appointment.model.DoctorProfile;
import com.example.doctor_appointment.model.DoctorSlot;
import com.example.doctor_appointment.repository.DoctorProfileRepository;
import com.example.doctor_appointment.repository.DoctorSlotRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class DoctorSlotService {

    @Autowired
    private DoctorProfileRepository doctorProfileRepository;

    @Autowired
    private DoctorSlotRepository doctorSlotRepository;

    @Scheduled(cron = "0 0 8 * * *")
    public void generateDailySlots() {
        List<DoctorProfile> doctors = doctorProfileRepository.findAll();
        LocalDate today = LocalDate.now();

        for (DoctorProfile doctor : doctors) {
            LocalTime start = doctor.getStartTime();
            LocalTime end = doctor.getEndTime();
            int duration = doctor.getSlotDuration();

            List<DoctorSlot> slots = new ArrayList<>();

            while (start.plusMinutes(duration).isBefore(end) || start.plusMinutes(duration).equals(end)) {
                DoctorSlot slot = new DoctorSlot();
                slot.setDoctor(doctor);
                slot.setDate(today);
                slot.setStartTime(start);
                slot.setEndTime(start.plusMinutes(duration));
                slot.setIsBooked(false);

                // check if already exists (optional due to @UniqueConstraint)
                if (!doctorSlotRepository.existsByDoctorAndDateAndStartTimeAndEndTime(
                        doctor, today, start, start.plusMinutes(duration))) {
                    slots.add(slot);
                }

                start = start.plusMinutes(duration);
            }

            doctorSlotRepository.saveAll(slots);
        }

        System.out.println("✅ Slots generated for all doctors at 8 AM");
    }

    public List<SlotResponse> getAllSlots() {
        return doctorSlotRepository.findAll().stream().map(slot -> {
            SlotResponse dto = new SlotResponse();
            dto.setId(slot.getId());
            dto.setDoctorName(slot.getDoctor().getUser().getName());
            dto.setDate(slot.getDate());
            dto.setStartTime(slot.getStartTime());
            dto.setEndTime(slot.getEndTime());
            dto.setIsBooked(slot.getIsBooked());
            return dto;
        }).toList();
    }

    public List<DoctorSlotResponse> getSlotsByDoctorId(Long doctorId) {
        List<DoctorSlot> doctorSlots = doctorSlotRepository.findByDoctorUserId(doctorId);
        return doctorSlots.stream().map
                (slot -> new DoctorSlotResponse(
                        slot.getId(),
                        slot.getDate(),
                        slot.getStartTime(),
                        slot.getEndTime(),
                        slot.getIsBooked()
                )).toList();
    }
}
