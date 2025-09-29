package com.marathicoder.dto;



public class TrainerEmailRequest {
    private String email;
    private String password;
    private String name;
    private String trainerId;

    public TrainerEmailRequest() {}

    public TrainerEmailRequest(String email, String password, String name, String trainerId) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.trainerId = trainerId;
    }

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getTrainerId() {
        return trainerId;
    }
    public void setTrainerId(String trainerId) {
        this.trainerId = trainerId;
    }
}