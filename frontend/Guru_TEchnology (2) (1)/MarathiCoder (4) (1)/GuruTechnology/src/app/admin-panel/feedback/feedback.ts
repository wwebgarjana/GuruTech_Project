import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { HttpClient, HttpClientModule } from '@angular/common/http';
import { finalize } from 'rxjs/operators';

interface FeedbackModel {
  id?: number;
  studentName?: string;
  studentId?: string;
  course?: string;
  batch?: string;
  lectureTitle?: string;
  rating?: number;
  comments?: string;
  createdAt?: string;
}

@Component({
  selector: 'app-feedback',
  standalone: true,
  imports: [CommonModule, FormsModule, HttpClientModule],
  templateUrl: './feedback.html',
  styleUrls: ['./feedback.css']
})
export class Feedback implements OnInit {
  courses = ['Java Full Stack', 'Python Backend', 'React.js'];
  batches = ['Batch A', 'Batch B', 'Batch C'];

  selectedCourse = '';
  selectedBatch = '';

  allFeedbacks: FeedbackModel[] = [];
  feedbacks: FeedbackModel[] = [];
  loading = false;
  error = '';

  private apiUrl = 'http://localhost:8082/api/feedback';

  constructor(private http: HttpClient) {}

  ngOnInit(): void {
    this.loadAll();
  }

  loadAll(): void {
    this.loading = true;
    this.error = '';
    this.http.get<FeedbackModel[]>(this.apiUrl)
      .pipe(finalize(() => this.loading = false))
      .subscribe({
        next: (data) => {
          this.allFeedbacks = data ?? [];
          this.applyFilters();
        },
        error: (err) => {
          console.error('Load error:', err);
          this.error = 'Failed to load feedbacks.';
        }
      });
  }

  applyFilters(): void {
    let list = [...this.allFeedbacks];
    if (this.selectedCourse) {
      list = list.filter(f => (f.course || '').toLowerCase() === this.selectedCourse.toLowerCase());
    }
    if (this.selectedBatch) {
      list = list.filter(f => (f.batch || '').toLowerCase() === this.selectedBatch.toLowerCase());
    }
    this.feedbacks = list.sort((a, b) => {
      const ta = a.createdAt ? Date.parse(a.createdAt) : 0;
      const tb = b.createdAt ? Date.parse(b.createdAt) : 0;
      return tb - ta;
    });
  }

  filter(): void {
    this.applyFilters();
  }

  resetFilters(): void {
    this.selectedCourse = '';
    this.selectedBatch = '';
    this.applyFilters();
  }

  getNumberArray(n?: number): number[] {
    const count = Math.max(0, Math.floor(n ?? 0));
    return Array.from({ length: count }, (_, i) => i);
  }
}
