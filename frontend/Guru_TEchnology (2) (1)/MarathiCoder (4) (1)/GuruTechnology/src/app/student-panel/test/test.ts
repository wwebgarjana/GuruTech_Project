




import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HttpClient, HttpClientModule, HttpHeaders } from '@angular/common/http';
import { FormsModule } from '@angular/forms';

interface Question {
  id: number;
  questionText: string;
  options: string[];
  correctAnswer: string;
  marks: number;
  selectedAnswer?: string | null;
}
interface Quiz {
  id: number;
  quizTitle: string;
  quizDescription: string;
  questions: Question[];
  completed?: boolean;
}

@Component({
  selector: 'app-test',
  standalone: true,
  imports: [CommonModule, HttpClientModule, FormsModule],
  templateUrl: './test.html',
  styleUrls: ['./test.css']
})
export class Test implements OnInit {
  testStarted = false;
  testSubmitted = false;
  scores: number[] = [];
  quizzes: Quiz[] = [];
  selectedQuiz: Quiz | null = null;
  selectedQuizIndex = -1;
  currentQuestionIndex = 0;
  loading = true;

  constructor(private http: HttpClient) {}

  ngOnInit() {
    this.fetchQuizzes();
  }

  fetchQuizzes() {
    const token = localStorage.getItem('token');
    if (!token) return;

    const headers = new HttpHeaders({ Authorization: `Bearer ${token}` });

    this.http.get<Quiz[]>('http://localhost:8082/api/quizzes/student', { headers })
      .subscribe({
        next: (data) => {
          this.quizzes = data.map((q) => ({
            id: q.id,
            quizTitle: q.quizTitle,
            quizDescription: q.quizDescription,
            questions: q.questions.map(qt => ({
              id: qt.id,
              questionText: qt.questionText,
              options: qt.options,
              correctAnswer: qt.correctAnswer,
              marks: qt.marks || 1,
              selectedAnswer: null
            })),
            completed: false
          }));
          this.scores = Array(this.quizzes.length).fill(0);
          this.loading = false;
        },
        error: (err) => {
          console.error('❌ Error fetching quizzes:', err);
          this.loading = false;
        }
      });
  }

  selectQuiz(quiz: Quiz, index: number) {
    if (quiz.completed) return;
    this.selectedQuiz = JSON.parse(JSON.stringify(quiz));
    this.selectedQuizIndex = index;
    this.testStarted = false;
    this.testSubmitted = false;
    this.currentQuestionIndex = 0;
  }

  startTest() {
    this.testStarted = true;
  }

  nextQuestion() {
    if (this.selectedQuiz && this.currentQuestionIndex < this.selectedQuiz.questions.length - 1) {
      this.currentQuestionIndex++;
    }
  }

  prevQuestion() {
    if (this.currentQuestionIndex > 0) {
      this.currentQuestionIndex--;
    }
  }

  submitTest() {
    if (!this.selectedQuiz) return;
    let score = 0;
    this.selectedQuiz.questions.forEach(q => {
      if (q.selectedAnswer === q.correctAnswer) score += q.marks;
    });

    this.scores[this.selectedQuizIndex] = score;
    this.testSubmitted = true;
    this.testStarted = false;
    this.quizzes[this.selectedQuizIndex].completed = true;

    // send to backend
    const token = localStorage.getItem('token');
    if (!token) return;
    const headers = new HttpHeaders({ Authorization: `Bearer ${token}` });
    const payload = {
      quizId: this.selectedQuiz.id,
      answers: this.selectedQuiz.questions.map(q => ({
        questionId: q.id,
        selectedAnswer: q.selectedAnswer
      }))
    };

    this.http.post('http://localhost:8082/api/quizzes/submit', payload, { headers })
      .subscribe({
        next: res => console.log('✅ Submission saved:', res),
        error: err => console.error('❌ Error saving submission:', err)
      });
  }

  goBackToList() {
    this.selectedQuiz = null;
    this.testSubmitted = false;
    this.testStarted = false;
    this.currentQuestionIndex = 0;
  }
}
