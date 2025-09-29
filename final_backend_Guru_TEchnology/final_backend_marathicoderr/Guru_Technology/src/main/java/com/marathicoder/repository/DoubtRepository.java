



package com.marathicoder.repository;

import com.marathicoder.model.Doubt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface DoubtRepository extends JpaRepository<Doubt, Long> {

    // fetch messages between two participants (either direction)
    List<Doubt> findBySenderNameAndReceiverNameOrReceiverNameAndSenderName(
            String sender1, String receiver1, String sender2, String receiver2);

    // return distinct student names (senderName) — legacy (not used for dropdown now)
    @Query("select distinct c.senderName from Doubt c where c.sender = 'student'")
    List<String> findDistinctStudents();
}
