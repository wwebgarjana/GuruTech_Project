package com.marathicoder.model;
 
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
 
@Entity
@Table(name = "student")
public class Student {
 
    @Id
    private String studentId;
 
    private String collegeId;
    private String name;
 
 private String batch;
    @Column(unique = true)
    private String aadhaar;
 
    @Column(unique = true)
    private String mobile;
 
    private String birthdate;
    private String address;
    private String course;
    private String email;
    private String password; // Include password field
 /////////////////////////////////pooja//////////////////
   private String certificateName;
 
    @Lob
    @Column(columnDefinition = "LONGBLOB")
    private byte[] uploadCertificate;
    @Lob
    private byte[] profileImage;
    
    private String paymentStatus = "pending"; // default
    private Double amount = 0.0;
 //////////////////////////
    // Getters and Setters
    
    
    public String getStudentId() { return studentId; }
    public String getBatch() {
		return batch;
	}
	public void setBatch(String batch) {
		this.batch = batch;
	}
	public String getPaymentStatus() {
		return paymentStatus;
	}
	public void setPaymentStatus(String paymentStatus) {
		this.paymentStatus = paymentStatus;
	}
	public Double getAmount() {
		return amount;
	}
	public void setAmount(Double amount) {
		this.amount = amount;
	}
	public String getCertificateName() {
		return certificateName;
	}
	public void setCertificateName(String certificateName) {
		this.certificateName = certificateName;
	}
	public byte[] getUploadCertificate() {
		return uploadCertificate;
	}
	public void setUploadCertificate(byte[] uploadCertificate) {
		this.uploadCertificate = uploadCertificate;
	}
	public byte[] getProfileImage() {
		return profileImage;
	}
	public void setProfileImage(byte[] profileImage) {
		this.profileImage = profileImage;
	}
	public void setStudentId(String studentId) { this.studentId = studentId; }
 
    public String getCollegeId() { return collegeId; }
    public void setCollegeId(String collegeId) { this.collegeId = collegeId; }
 
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
 
    public String getCourse() { return course; }
    public void setCourse(String course) { this.course = course; }
 
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
 
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
	public String getId() {
		// TODO Auto-generated method stub
		return null;
	}
}
 