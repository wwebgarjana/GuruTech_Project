


package com.marathicoder.service;

import com.marathicoder.model.Doubt;
import com.marathicoder.repository.DoubtRepository;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DoubtService {

    private final DoubtRepository repo;

    public DoubtService(DoubtRepository repo) {
        this.repo = repo;
    }

    public Doubt save(Doubt msg) {
        return repo.save(msg);
    }

//    public List<Doubt> getConversation(String user1, String user2) {
//        List<Doubt> list = repo.findBySenderNameAndReceiverNameOrReceiverNameAndSenderName(
//                user1, user2, user1, user2);
//        list.sort(Comparator.comparing(m -> m.getSentAt() == null ? java.time.LocalDateTime.MIN : m.getSentAt()));
//        return list;
//    }
    
    
    public List<Doubt> getConversation(String studentEmail, String trainerEmail) {
        return repo.findAll().stream()
            .filter(d ->
                (d.getSenderName().equals(studentEmail) && d.getReceiverName().equals(trainerEmail)) ||
                (d.getSenderName().equals(trainerEmail) && d.getReceiverName().equals(studentEmail))
            )
            .sorted(Comparator.comparing(Doubt::getSentAt)) // optional: sort by timestamp
            .collect(Collectors.toList());
    }

}

