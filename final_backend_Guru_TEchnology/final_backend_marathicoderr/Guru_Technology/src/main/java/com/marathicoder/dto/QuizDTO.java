package com.marathicoder.dto;

import com.marathicoder.model.Question;
import java.util.List;

public class QuizDTO {
    private Long id;
    private String title;
    private String description;
    private List<Question> questions;

    public QuizDTO(Long id, String title, String description, List<Question> questions) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.questions = questions;
    }

    // Getters
    public Long getId() { return id; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public List<Question> getQuestions() { return questions; }
}
