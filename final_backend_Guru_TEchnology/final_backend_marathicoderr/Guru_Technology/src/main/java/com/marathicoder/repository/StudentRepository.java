package com.marathicoder.repository;
 
import com.marathicoder.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
 
public interface StudentRepository extends JpaRepository<Student, Long> {
    Optional<Student> findByEmail(String email);
    Optional<Student> findByStudentId(Long StudentId);
 
    // ✅ Uniqueness checks
    boolean existsByMobile(String mobile);
    boolean existsByAadhaar(String aadhaar);
    
    @Query(value = "SELECT student_id FROM student ORDER BY student_id DESC LIMIT 1", nativeQuery = true)
    String findLastStudentId();
    
    
    //////////////pooja/////////////////
    List<Student> findByCourse(String course);
	Optional<Student> findByName(String studentName);
	Optional<Student> findByStudentId(String id);
 
	@Query("SELECT s FROM Student s WHERE LOWER(s.email) = LOWER(:email)")
    Optional<Student> findByEmailIgnoreCase(@Param("email") String email);
 
}