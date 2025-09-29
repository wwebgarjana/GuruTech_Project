package com.marathicoder.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.marathicoder.model.NotificationEntity;

import java.util.List;

public interface NotificationRepository extends JpaRepository<NotificationEntity, Long> {
    
    List<NotificationEntity> findByReceiverRoleAndReceiverEmail(String receiverRole, String receiverEmail);
    List<NotificationEntity> findByReceiverRoleAndBatch(String receiverRole, String batch);
}
