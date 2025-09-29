package com.marathicoder.service;



import com.marathicoder.model.RevokedUser;
import com.marathicoder.repository.RevokedUserRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class RevokedUserService {
    private final RevokedUserRepository repository;

    public RevokedUserService(RevokedUserRepository repository) {
        this.repository = repository;
    }

    public List<RevokedUser> getAll() {
        return repository.findAll();
    }
}
