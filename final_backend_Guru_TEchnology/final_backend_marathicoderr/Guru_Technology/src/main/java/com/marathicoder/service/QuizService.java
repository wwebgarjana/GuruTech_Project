package com.marathicoder.service;

import com.marathicoder.model.Quiz;
import com.marathicoder.repository.QuizRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;

@Service
public class QuizService {

    @Autowired
    private QuizRepository quizRepository;

    public List<Quiz> getAllQuizzes() { return quizRepository.findAll(); }
    public List<Quiz> getQuizzesByTrainer(String trainerId) { return quizRepository.findByTrainerId(trainerId); }
    public List<Quiz> getQuizzesByStudentEmail(String studentEmail) { return quizRepository.findByStudentEmail(studentEmail); }
    public Optional<Quiz> getQuizById(Long id) { return quizRepository.findById(id); }

    @Transactional
    public Quiz createQuiz(Quiz quiz) { return quizRepository.save(quiz); }

    public List<Quiz> getQuizzesByStudent(String studentId) { return quizRepository.findByStudentId(studentId); }
}