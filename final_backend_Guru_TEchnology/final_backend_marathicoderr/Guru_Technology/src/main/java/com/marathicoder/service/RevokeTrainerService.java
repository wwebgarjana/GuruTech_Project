


package com.marathicoder.service;

import com.marathicoder.model.Trainer;
import com.marathicoder.model.RevokedUser; // new import
import com.marathicoder.repository.RevokeTrainerRepository;
import com.marathicoder.repository.RevokedUserRepository; // new import
import com.marathicoder.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RevokeTrainerService {

    @Autowired
    private RevokeTrainerRepository trainerRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RevokedUserRepository revokedUserRepository; // new repository

    public String revokeAccess(String trainerId, String email) {
        Optional<Trainer> trainerOpt = trainerRepository.findByTrainerIdAndEmail(trainerId, email);

        if (trainerOpt.isPresent()) {
            Trainer trainer = trainerOpt.get();

            // 🔹 New functionality: Save in revoked_users table before deletion
            RevokedUser revoked = new RevokedUser();
            revoked.setOriginalId(trainer.getTrainerId());
            revoked.setEmail(trainer.getEmail());
            revoked.setRole("TRAINER");
            revoked.setRevokedBy("ADMIN"); // you can customize as needed
            revokedUserRepository.save(revoked);

            // 1. Delete Trainer record
            trainerRepository.delete(trainer);

            // 2. Delete from users table also
            if (userRepository.findByEmail(email) != null) {
                userRepository.delete(userRepository.findByEmail(email));
            }

            return "✅ Trainer deleted successfully from both trainers & users tables for ID: " + trainerId;
        } else {
            return "❌ Trainer not found with given ID and Email";
        }
    }
}
