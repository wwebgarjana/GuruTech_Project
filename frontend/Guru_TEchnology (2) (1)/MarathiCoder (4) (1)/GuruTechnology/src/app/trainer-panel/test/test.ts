


import { Component, OnInit } from '@angular/core';
import { FormArray, FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { HttpClient, HttpHeaders, HttpClientModule } from '@angular/common/http';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-test',
  standalone: true,
  templateUrl: './test.html',
  styleUrls: ['./test.css'],
  imports: [CommonModule, ReactiveFormsModule, FormsModule, HttpClientModule]
})
export class Test implements OnInit {
  quizForm!: FormGroup;
  assignedBatches: string[] = [];
  assignedCourses: string[] = [];

  constructor(private fb: FormBuilder, private http: HttpClient) {}

  ngOnInit(): void {
    this.quizForm = this.fb.group({
      batch: ['', Validators.required],
      course: ['', Validators.required],
      quizTitle: ['', Validators.required],
      quizDescription: ['', Validators.required],
      questions: this.fb.array([this.createQuestion()])
    });

    this.fetchTrainerAssignments();
  }

  get questionArray(): FormArray {
    return this.quizForm.get('questions') as FormArray;
  }

  createQuestion(): FormGroup {
    return this.fb.group({
      questionText: ['', Validators.required],
      marks: [1, [Validators.required, Validators.min(1)]],
      options: this.fb.array([
        this.fb.control('', Validators.required),
        this.fb.control('', Validators.required),
        this.fb.control('', Validators.required),
        this.fb.control('', Validators.required)
      ]),
      correctAnswer: ['', Validators.required]
    });
  }

  getOptions(questionIndex: number): FormArray {
    return this.questionArray.at(questionIndex).get('options') as FormArray;
  }

  addQuestion(): void {
    this.questionArray.push(this.createQuestion());
  }

  submitQuiz(): void {
    if (!this.quizForm.valid) {
      alert('Please fill in all required fields.');
      return;
    }

    const batch = this.quizForm.get('batch')?.value;
    const url = `http://localhost:8082/api/quizzes/create/batch?batch=${encodeURIComponent(batch)}`;
    const token = localStorage.getItem('token');

    if (!token) {
      alert('❌ No token found! Please login again.');
      return;
    }

    const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);

    this.http.post(url, this.quizForm.value, { headers })
      .subscribe({
        next: () => {
          alert('✅ Quiz saved successfully!');
          this.quizForm.reset();
          this.questionArray.clear();
          this.addQuestion();
        },
        error: (err) => {
          console.error('❌ Error saving quiz', err);
          alert('Failed to save quiz!');
        }
      });
  }

  fetchTrainerAssignments(): void {
    const trainerEmail = localStorage.getItem("userEmail");
    if (!trainerEmail) {
      console.warn("⚠️ No trainerEmail found in localStorage. Please login again.");
      return;
    }

    const apiUrl = `http://localhost:8082/api/trainers/email/${encodeURIComponent(trainerEmail)}/assignments`;

    this.http.get<any>(apiUrl).subscribe({
      next: (res) => {
        this.assignedBatches = res.assignments ? Array.from(new Set(res.assignments.map((a: any) => String(a.batch)))) : [];
        this.assignedCourses = res.assignments ? Array.from(new Set(res.assignments.map((a: any) => String(a.course)))) : [];
      },
      error: (err) => console.error("❌ Assignments API Error:", err)
    });
  }
}
