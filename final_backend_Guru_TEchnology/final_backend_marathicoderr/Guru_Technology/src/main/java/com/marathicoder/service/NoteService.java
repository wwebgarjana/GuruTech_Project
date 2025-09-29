package com.marathicoder.service;

import com.marathicoder.model.Note;
import com.marathicoder.repository.NoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class NoteService {

    @Autowired
    private NoteRepository noteRepository;

    public Note uploadNote(String title, String description, MultipartFile file,
                           String trainerId, String studentId, String studentEmail) throws IOException {
        Note note = new Note();
        note.setTitle(title);
        note.setDescription(description);
        note.setFileName(file.getOriginalFilename());
        note.setFileData(file.getBytes());

        note.setTrainerId(trainerId);
        note.setStudentId(studentId);
        note.setStudentEmail(studentEmail);

        return noteRepository.save(note);
    }

    public List<Note> getAllNotes() {
        return noteRepository.findAll();
    }

    public List<Note> getNotesByTrainer(String trainerId) {
        return noteRepository.findByTrainerId(trainerId);
    }

    public List<Note> getNotesByStudent(String studentId) {
        return noteRepository.findByStudentId(studentId);
    }

    public List<Note> getNotesByStudentEmail(String studentEmail) {
        return noteRepository.findByStudentEmail(studentEmail);
    }

    public Optional<Note> getNoteById(Long id) {
        return noteRepository.findById(id);
    }
}
