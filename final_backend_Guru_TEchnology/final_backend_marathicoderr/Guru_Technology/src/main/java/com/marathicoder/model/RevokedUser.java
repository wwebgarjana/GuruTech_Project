package com.marathicoder.model;


import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "revoked_users")
public class RevokedUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String originalId; // studentId or trainerId
    private String email;
    private String role; // STUDENT / TRAINER
    private String revokedBy; // admin
    private LocalDateTime revokedOn = LocalDateTime.now();
    private String reason; // optional

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getOriginalId() { return originalId; }
    public void setOriginalId(String originalId) { this.originalId = originalId; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public String getRevokedBy() { return revokedBy; }
    public void setRevokedBy(String revokedBy) { this.revokedBy = revokedBy; }

    public LocalDateTime getRevokedOn() { return revokedOn; }
    public void setRevokedOn(LocalDateTime revokedOn) { this.revokedOn = revokedOn; }

    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }
}
