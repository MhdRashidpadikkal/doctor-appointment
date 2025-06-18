package com.example.doctor_appointment.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendOtpEmail(String toEmail, String otp) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("🔐 Verify Your Email for Doctor Appointment Portal");

        String body = String.format("""
        Hello,

        Thank you for registering with the Doctor Appointment System.

        Please use the OTP below to verify your email address. This OTP is valid for 10 minutes:

        👉 Your OTP: %s

        If you did not initiate this request, please ignore this email.

        Best regards,
        Doctor Appointment Team
    """, otp);

        message.setText(body);
        mailSender.send(message);
    }
}

