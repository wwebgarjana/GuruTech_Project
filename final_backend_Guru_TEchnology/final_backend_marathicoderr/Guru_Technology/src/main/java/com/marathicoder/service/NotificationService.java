package com.marathicoder.service;

import org.springframework.stereotype.Service;

import com.marathicoder.model.NotificationEntity;
import com.marathicoder.repository.NotificationRepository;

import java.util.List;
import java.util.Optional;

@Service
public class NotificationService {
    private final NotificationRepository repo;

    public NotificationService(NotificationRepository repo) {
        this.repo = repo;
    }

    public NotificationEntity send(NotificationEntity n) {
        return repo.save(n);
    }

    public List<NotificationEntity> getTrainerNotifications(String email) {
        return repo.findByReceiverRoleAndReceiverEmail("TRAINER", email);
    }

    public List<NotificationEntity> getStudentNotifications(String email) {
        return repo.findByReceiverRoleAndReceiverEmail("STUDENT", email);
    }

    public List<NotificationEntity> getAdminNotifications(String email) {
        return repo.findByReceiverRoleAndReceiverEmail("ADMIN", email);
    }
    public Optional<NotificationEntity> getById(Long id) {
        return repo.findById(id);
    }

	public List<NotificationEntity> getNotificationsByReceiverEmail(String email) {
		// TODO Auto-generated method stub
		return null;
	}

	
}