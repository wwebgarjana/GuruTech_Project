




package com.marathicoder.model;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "doubts")
public class Doubt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // "student" or "trainer"
    private String sender;

    // sender display name or id
    private String senderName;

    // receiver display name or id (we'll use trainer name or student name)
    private String receiverName;

    @Column(columnDefinition = "TEXT")
    private String message;

    private LocalDateTime sentAt;

    public Doubt() {}

    @PrePersist
    public void prePersist() {
        this.sentAt = LocalDateTime.now();
    }

    // getters / setters
    public Long getId() { return id; }
    public String getSender() { return sender; }
    public void setSender(String sender) { this.sender = sender; }
    public String getSenderName() { return senderName; }
    public void setSenderName(String senderName) { this.senderName = senderName; }
    public String getReceiverName() { return receiverName; }
    public void setReceiverName(String receiverName) { this.receiverName = receiverName; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    public LocalDateTime getSentAt() { return sentAt; }
    public void setSentAt(LocalDateTime sentAt) { this.sentAt = sentAt; }

	public String getBatch() {
		// TODO Auto-generated method stub
		return null;
	}
}
