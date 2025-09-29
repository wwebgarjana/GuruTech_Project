package com.marathicoder.dto;
 
import java.util.List;
 
public class EducationDetailsDTO {
    private String email;
    private List<EducationDetailsItem> educationList;
 
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
 
    public List<EducationDetailsItem> getEducationList() { return educationList; }
    public void setEducationList(List<EducationDetailsItem> educationList) { this.educationList = educationList; }
 
    public static class EducationDetailsItem {
        private String qualification;
        private String major;
        private String institute;
        private String university;
        private String year;
        private String cgpa;
        private String type;
        private String certifications;
 
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
}
 