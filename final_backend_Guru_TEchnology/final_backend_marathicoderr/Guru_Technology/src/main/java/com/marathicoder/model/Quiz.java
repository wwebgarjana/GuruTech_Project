
package com.marathicoder.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "quiz")
public class Quiz {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String quizTitle;
    private String quizDescription;

    private String trainerId;
    private String studentId;
    private String studentEmail;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "quiz_id")
    private List<Question> questions = new ArrayList<>();

    public Quiz() {}

    public Quiz(String quizTitle, String quizDescription, String trainerId, String studentId, String studentEmail, List<Question> questions) {
        this.quizTitle = quizTitle;
        this.quizDescription = quizDescription;
        this.trainerId = trainerId;
        this.studentId = studentId;
        this.studentEmail = studentEmail;
        this.questions = questions;
    }

    // Getters & Setters
    public Long getId() { return id; }
    public String getQuizTitle() { return quizTitle; }
    public void setQuizTitle(String quizTitle) { this.quizTitle = quizTitle; }
    public String getQuizDescription() { return quizDescription; }
    public void setQuizDescription(String quizDescription) { this.quizDescription = quizDescription; }
    public String getTrainerId() { return trainerId; }
    public void setTrainerId(String trainerId) { this.trainerId = trainerId; }
    public String getStudentId() { return studentId; }
    public void setStudentId(String studentId) { this.studentId = studentId; }
    public String getStudentEmail() { return studentEmail; }
    public void setStudentEmail(String studentEmail) { this.studentEmail = studentEmail; }
    public List<Question> getQuestions() { return questions; }
    public void setQuestions(List<Question> questions) { this.questions = questions; }
}