package com.marathicoder.repository;



import com.marathicoder.model.RevokedUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RevokedUserRepository extends JpaRepository<RevokedUser, Long> {
}
