package com.marathicoder.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.marathicoder.dto.CounsellingForm;
import com.marathicoder.service.MailService;

@RestController
@RequestMapping("/api/form")
@CrossOrigin(origins = "http://localhost:4200") // specify exact frontend URL
public class FormController {
    @Autowired
    private MailService mailService;

    @PostMapping("/submit")
    public String submitForm(@RequestBody CounsellingForm form) {
        mailService.sendFormSubmissionEmail(
            form.getFullName(),
            form.getEmail(),
            form.getPhone(),
            form.getCourse(),
            form.getAppointment()
        );
        return "✅ Form submitted successfully! Mail sent.";
    }
}

