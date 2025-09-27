import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, NgForm } from '@angular/forms';
import { HttpClient, HttpClientModule, HttpHeaders } from '@angular/common/http';
import { finalize } from 'rxjs/operators';

interface FeedbackPayload {
  studentName: string;
  studentId: string;
  course: string;
  batch: string;
  lectureTitle: string;
  rating: number;
  comments: string;
}

@Component({
  selector: 'app-feedback',
  standalone: true,
  imports: [CommonModule, FormsModule, HttpClientModule],
  templateUrl: './feedback.html',
  styleUrls: ['./feedback.css']
})
export class Feedback implements OnInit {
  // Model for the form
  model: FeedbackPayload = {
    studentName: '',
    studentId: '',
    course: '',
    batch: '',
    lectureTitle: '',
    rating: 5,
    comments: ''
  };

  // UI state
  message = '';
  submitting = false;

  // Dropdown options
  assignedBatches: string[] = [];
  assignedCourses: string[] = [];

  private apiUrl = 'http://localhost:8082/api/feedback';

  constructor(private http: HttpClient) {}

  ngOnInit(): void {
    this.fetchStudentAssignments();
  }

  

  // Submit feedback
  onSubmit(form: NgForm) {
    this.message = '';
    if (form.invalid) {
      this.message = '❌ Please fill all required fields.';
      return;
    }

    const token = localStorage.getItem('token');
    if (!token) {
      this.message = '❌ You are not logged in. Please log in first.';
      return;
    }

    const headers = new HttpHeaders({ Authorization: `Bearer ${token}` });

    this.submitting = true;
    this.http.post(this.apiUrl, this.model, { headers })
      .pipe(finalize(() => this.submitting = false))
      .subscribe({
        next: () => {
          this.message = '✅ Feedback submitted successfully!';
          form.resetForm({ rating: 5 });
        },
        error: (err) => {
          console.error('❌ Submit error:', err);
          this.message = '❌ Failed to submit feedback. Only assigned trainer can receive.';
        }
      });
  }

  fetchStudentAssignments(): void {
  const studentEmail = localStorage.getItem('userEmail');
  if (!studentEmail) return;

  const url = `http://localhost:8082/api/students/email/${encodeURIComponent(studentEmail)}/assignments`;

  this.http.get<any>(url).subscribe({
    next: (res) => {
      console.log('✅ Assignments API Response:', res);

      // Set student name & ID automatically
      this.model.studentName = res.studentName || '';
      this.model.studentId = res.studentId || '';

      // Populate batch & course dropdowns
      this.assignedBatches = res.assignments
        ? Array.from(new Set(res.assignments.map((a: any) => a.batch)))
        : [];
      this.assignedCourses = res.assignments
        ? Array.from(new Set(res.assignments.map((a: any) => a.course)))
        : [];

      console.log('📌 Student Name:', this.model.studentName);
      console.log('📌 Student ID:', this.model.studentId);
      console.log('📌 Batches:', this.assignedBatches);
      console.log('📌 Courses:', this.assignedCourses);
    },
    error: (err) => console.error('❌ Assignments fetch error:', err)
  });
}

}
