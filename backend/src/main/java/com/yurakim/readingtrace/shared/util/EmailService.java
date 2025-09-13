package com.yurakim.readingtrace.shared.util;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    public void sendPasswordRestEmail(String recipient, String resetUrl) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(recipient);
        message.setSubject("[ReadTrace] Password Reset Request");
        message.setText("Please click the link below to reset your password:\n\n" + resetUrl);
        mailSender.send(message);
    }

}