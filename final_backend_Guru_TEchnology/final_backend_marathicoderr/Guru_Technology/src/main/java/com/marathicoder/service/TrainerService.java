package com.marathicoder.service;


	
	
	import org.springframework.beans.factory.annotation.Autowired;
	import org.springframework.stereotype.Service;

import com.marathicoder.model.Trainer;
import com.marathicoder.repository.TrainerRepository;

	@Service
	public class TrainerService {

	    @Autowired
	    private TrainerRepository repo;

	    public String getLastTrainerId() {
	        String lastId = repo.findLastTrainerId();
	        return lastId != null ? lastId : "TG000"; // fallback if table empty
	    }

//	    public Trainer saveTrainer(Trainer trainer) {
//	        return repo.save(trainer);
//	    }
//
//	    public boolean emailExists(String email) {
//	        return repo.existsByEmail(email);
//	    }
	}



