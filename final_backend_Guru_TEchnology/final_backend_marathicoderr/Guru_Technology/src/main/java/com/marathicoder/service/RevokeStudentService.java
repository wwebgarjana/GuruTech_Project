


package com.marathicoder.service;

import com.marathicoder.model.Student;
import com.marathicoder.model.RevokedUser; // new import
import com.marathicoder.repository.RevokeStudentRepository;
import com.marathicoder.repository.RevokedUserRepository; // new import
import com.marathicoder.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RevokeStudentService {

    @Autowired
    private RevokeStudentRepository studentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RevokedUserRepository revokedUserRepository; // new repository

    public String revokeAccess(String studentId, String email) {
        Optional<Student> studentOpt = studentRepository.findByStudentIdAndEmail(studentId, email);

        if (studentOpt.isPresent()) {
            Student student = studentOpt.get();

            // 🔹 New functionality: Save in revoked_users table before deletion
            RevokedUser revoked = new RevokedUser();
            revoked.setOriginalId(student.getStudentId());
            revoked.setEmail(student.getEmail());
            revoked.setRole("STUDENT");
            revoked.setRevokedBy("ADMIN"); // you can customize as needed
            revokedUserRepository.save(revoked);

            // 1. Delete Student record
            studentRepository.delete(student);

            // 2. Delete from users table also
            if (userRepository.findByEmail(email) != null) {
                userRepository.delete(userRepository.findByEmail(email));
            }

            return "✅ Student deleted successfully from both students & users tables for ID: " + studentId;
        } else {
            return "❌ Student not found with given ID and Email";
        }
    }
}

