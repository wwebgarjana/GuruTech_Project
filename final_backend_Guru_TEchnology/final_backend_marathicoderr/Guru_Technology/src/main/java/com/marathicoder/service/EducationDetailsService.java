package com.marathicoder.service;
 
 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
 
import com.marathicoder.dto.EducationDetailsDTO;
import com.marathicoder.model.EducationDetails;
import com.marathicoder.repository.EducationDetailsRepository;
 
import java.util.ArrayList;
import java.util.List;
 
@Service
public class EducationDetailsService {
 
    @Autowired
    private EducationDetailsRepository educationDetailsRepository;
 
    @Transactional
    public void saveEducationDetails(EducationDetailsDTO dto) {
        List<EducationDetails> entities = new ArrayList<>();
        for (EducationDetailsDTO.EducationDetailsItem item : dto.getEducationList()) {
            EducationDetails edu = new EducationDetails();
            edu.setEmail(dto.getEmail());
            edu.setQualification(item.getQualification());
            edu.setMajor(item.getMajor());
            edu.setInstitute(item.getInstitute());
            edu.setUniversity(item.getUniversity());
            edu.setYear(item.getYear());
            edu.setCgpa(item.getCgpa());
            edu.setType(item.getType());
            edu.setCertifications(item.getCertifications());
            entities.add(edu);
        }
 
        educationDetailsRepository.saveAll(entities); // ✅ No delete, only append
    }
 
    public List<EducationDetails> getEducationByEmail(String email) {
        return educationDetailsRepository.findByEmail(email);
    }
 
    public List<EducationDetails> getAll() {
        return educationDetailsRepository.findAll();
    }
}