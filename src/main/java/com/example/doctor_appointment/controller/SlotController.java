package com.example.doctor_appointment.controller;

import com.example.doctor_appointment.dto.DoctorSlotResponse;
import com.example.doctor_appointment.dto.SlotResponse;
import com.example.doctor_appointment.model.DoctorSlot;
import com.example.doctor_appointment.service.DoctorSlotService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/slots")
@Tag(name = "Doctor Slot Controller", description = "Doctor slot generation and viewing")
public class SlotController {
    @Autowired
    private DoctorSlotService doctorSlotService;

    @GetMapping
    public ResponseEntity<List<SlotResponse>> getAllSlots() {
        return ResponseEntity.ok(doctorSlotService.getAllSlots());
    }

    @GetMapping("/doctor/{id}")
    public ResponseEntity<List<DoctorSlotResponse>> getDoctorSlots(@PathVariable Long id) {
        return ResponseEntity.ok(doctorSlotService.getSlotsByDoctorId(id));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/generate")
    public ResponseEntity<String> generateTodaySlots() {
        doctorSlotService.generateDailySlots();
        return ResponseEntity.ok("Slots generated manually for today.");
    }

}
