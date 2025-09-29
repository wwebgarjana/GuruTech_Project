package com.marathicoder.model;
 
import java.time.LocalDateTime;
import javax.persistence.*;
import com.fasterxml.jackson.annotation.JsonFormat;
 
@Entity
@Table(name = "job_posts")
public class JobPost {
 
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
 
    private String title;
    private String companyName;
 
    @Column(length = 1000)
    private String description;
 
    private Double salaryPackage;
    private String course;
 
    @Column(nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdDate;
 
    // Automatically set createdDate when saving
    @PrePersist
    public void prePersist() {
        createdDate = LocalDateTime.now();
    }
 
	public Long getId() {
		return id;
	}
 
	public void setId(Long id) {
		this.id = id;
	}
 
	public String getTitle() {
		return title;
	}
 
	public void setTitle(String title) {
		this.title = title;
	}
 
	public String getCompanyName() {
		return companyName;
	}
 
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
 
	public String getDescription() {
		return description;
	}
 
	public void setDescription(String description) {
		this.description = description;
	}
 
	public Double getSalaryPackage() {
		return salaryPackage;
	}
 
	public void setSalaryPackage(Double salaryPackage) {
		this.salaryPackage = salaryPackage;
	}
 
	public String getCourse() {
		return course;
	}
 
	public void setCourse(String course) {
		this.course = course;
	}
 
	public LocalDateTime getCreatedDate() {
		return createdDate;
	}
 
	public void setCreatedDate(LocalDateTime createdDate) {
		this.createdDate = createdDate;
	}
 
    // Getters and Setters
}
 
 