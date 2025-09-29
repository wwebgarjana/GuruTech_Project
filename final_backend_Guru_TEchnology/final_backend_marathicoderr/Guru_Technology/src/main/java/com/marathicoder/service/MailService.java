package com.marathicoder.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class MailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendFormSubmissionEmail(String studentName, String studentEmail, String phone, String course, String appointment) {
        String subject = "New Course Form Submission";
        String body = "Student Name: " + studentName +
                      "\nEmail: " + studentEmail +
                      "\nPhone: " + phone +
                      "\nCourse: " + course +
                      "\nAppointment: " + appointment;

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("gurutechpune@gmail.com");  // your Gmail
        message.setTo("gurutechpune@gmail.com");    // admin Gmail
        message.setSubject(subject);
        message.setText(body);

        mailSender.send(message);
    }
}
