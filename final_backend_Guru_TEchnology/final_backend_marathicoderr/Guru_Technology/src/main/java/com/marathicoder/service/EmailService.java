package com.marathicoder.service;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendStudentWelcomeEmail(String toEmail, String fullName, String studentId, String password) {
        String subject = "🎓 Welcome to Guru Technology";

        String body = "Dear " + fullName + ",\n\n" +
                "Welcome to Guru Technology! 🎓\n\n" +
                "Your student registration is complete. Please find your login credentials below:\n\n" +
                "🔹 Student ID: " + studentId + "\n" +
                "🔹 Login Email: " + toEmail + "\n" +
                "🔹 Temporary Password: " + password + "\n\n" +
                "You can now log in to the student portal using the above credentials.\n\n" +
                "Regards,\n" +
                "Admissions Team\n" +
                "Web Garjana Education";

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("gurutechpune@gmail.com"); 
        message.setTo(toEmail);
        message.setSubject(subject);
        message.setText(body);

        mailSender.send(message);
    }
    
    public void sendTrainerWelcomeEmail(String toEmail, String fullName, String trainerId, String password) {
        String subject = "📚 Welcome to Guru_Technology - Trainer Access";

        String body = "Dear " + fullName + ",\n\n" +
                "Welcome to Guru Technology! 📚\n\n" +
                "Your trainer profile has been successfully created. Please find your login credentials below:\n\n" +
                "🔹 Trainer ID: " + trainerId + "\n" +
                "🔹 Login Email: " + toEmail + "\n" +
                "🔹 Temporary Password: " + password + "\n\n" +
                "You can now log in to the trainer portal using the above credentials.\n\n" +
                "Regards,\n" +
                "HR Team\n" +
                "Web Garjana Education";

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("hr@webgarjana.com");
        message.setTo(toEmail);
        message.setSubject(subject);
        message.setText(body);

        mailSender.send(message);
    }

}