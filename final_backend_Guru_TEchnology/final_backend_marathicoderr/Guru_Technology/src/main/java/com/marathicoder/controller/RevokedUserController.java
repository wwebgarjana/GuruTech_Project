package com.marathicoder.controller;

import com.marathicoder.model.RevokedUser;
import com.marathicoder.service.RevokedUserService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/revoked-users")
@CrossOrigin(origins = "http://localhost:4200")   // adjust as needed
public class RevokedUserController {

    private final RevokedUserService service;

    public RevokedUserController(RevokedUserService service) {
        this.service = service;
    }

    @GetMapping
    public List<RevokedUser> getAllRevokedUsers() {
        return service.getAll();
    }
}
