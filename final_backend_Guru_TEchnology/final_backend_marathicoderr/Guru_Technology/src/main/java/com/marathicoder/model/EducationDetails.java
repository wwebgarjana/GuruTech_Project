package com.marathicoder.model;
 
import javax.persistence.*;
 
@Entity
@Table(name = "education_details")
public class EducationDetails {
 
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
 
    private String email;
    private String qualification;
    private String major;
    private String institute;
    private String university;
    private String year;
    private String cgpa;
    private String type;
    private String certifications;
 
    // Getters & Setters
 
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
 
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
 
    public String getQualification() { return qualification; }
    public void setQualification(String qualification) { this.qualification = qualification; }
 
    public String getMajor() { return major; }
    public void setMajor(String major) { this.major = major; }
 
    public String getInstitute() { return institute; }
    public void setInstitute(String institute) { this.institute = institute; }
 
    public String getUniversity() { return university; }
    public void setUniversity(String university) { this.university = university; }
 
    public String getYear() { return year; }
    public void setYear(String year) { this.year = year; }
 
    public String getCgpa() { return cgpa; }
    public void setCgpa(String cgpa) { this.cgpa = cgpa; }
 
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
 
    public String getCertifications() { return certifications; }
    public void setCertifications(String certifications) { this.certifications = certifications; }
}
 