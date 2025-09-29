package com.marathicoder.dto;



import java.util.List;

public class EducationUpdateRequest {
    private String email;
    private List<String> education;

    // getters & setters
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public List<String> getEducation() {
        return education;
    }
    public void setEducation(List<String> education) {
        this.education = education;
    }
}
