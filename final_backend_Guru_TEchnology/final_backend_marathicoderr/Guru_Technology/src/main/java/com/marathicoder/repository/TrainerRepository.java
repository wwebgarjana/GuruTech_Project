package com.marathicoder.repository;

import com.marathicoder.model.Trainer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;


	public interface TrainerRepository extends JpaRepository<Trainer, Long> {

	    Optional<Trainer> findByEmail(String email);

	    Optional<Trainer> findByTrainerId(String trainerId);

	    boolean existsByEmail(String email);

	    boolean existsByMobile(String mobile);

	    boolean existsByAadhaar(String aadhaar);

	    Trainer findTopByOrderByTrainerIdDesc();

	    @Query(value = "SELECT trainer_id FROM trainer ORDER BY trainer_id DESC LIMIT 1", nativeQuery = true)
	    String findLastTrainerId();

		Optional<Trainer> findByName(String trainerNameOrId);
	}

	

