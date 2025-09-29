package com.marathicoder.model;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "trainer")
public class Trainer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "trainer_id", unique = true)
  
    private String trainerId;

    private String name;
    private String aadhaar;
    private String mobile;
    private String birthdate;
    private String address;
    private String collegeId;
    @OneToMany(mappedBy = "trainer", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<CourseBatch> courseBatches = new ArrayList<>();
   
    private String courses;

    private String batches;

    @Column(unique = true)
    private String email;

    private String password;
    private String status;
   
    private String startDate;
    private String endDate;


    @ElementCollection
    @CollectionTable(name = "trainer_education", joinColumns = @JoinColumn(name = "trainer_id"))
    @Column(name = "education")
    private List<String> education;

    
	@ElementCollection
    @CollectionTable(name = "trainer_experience", joinColumns = @JoinColumn(name = "trainer_id"))
    @Column(name = "experience")
    private List<String> experience;

    // ---------- Getters & Setters ----------
	public List<CourseBatch> getCourseBatches() {
	    return courseBatches;
	}
	public void setCourseBatches(List<CourseBatch> courseBatches) {
	    this.courseBatches = courseBatches;
	}
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTrainerId() { return trainerId; }
    public void setTrainerId(String trainerId) { this.trainerId = trainerId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getAadhaar() { return aadhaar; }
    public void setAadhaar(String aadhaar) { this.aadhaar = aadhaar; }

    public String getMobile() { return mobile; }
    public void setMobile(String mobile) { this.mobile = mobile; }

    public String getBirthdate() { return birthdate; }
    public void setBirthdate(String birthdate) { this.birthdate = birthdate; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getCollegeId() { return collegeId; }
    public void setCollegeId(String collegeId) { this.collegeId = collegeId; }

   

    public String getCourses() {
		return courses;
	}
	public void setCourses(String courses) {
		this.courses = courses;
	}
	public String getBatches() {
		return batches;
	}
	public void setBatches(String batches) {
		this.batches = batches;
	}
	public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public List<String> getEducation() { return education; }
    public void setEducation(List<String> education) { this.education = education; }

    public List<String> getExperience() { return experience; }
    public void setExperience(List<String> experience) { this.experience = experience; }
    
    public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
}
