



import { Component, OnInit } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-videos',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './videos.html',
  styleUrls: ['./videos.css']
})
export class Videos implements OnInit {
  fetchedVideos: any[] = [];
  loading = false;
  errorMsg = '';

  constructor(private http: HttpClient) {}

  ngOnInit(): void {
    this.fetchStudentVideos();
  }

  fetchStudentVideos() {
    const token = localStorage.getItem('token');  // ✅ same key as schedule
    if (!token) {
      this.errorMsg = "❌ Please login first!";
      return;
    }

    this.loading = true;
    this.errorMsg = '';

    const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);

    this.http.get<any[]>("http://localhost:8082/api/videos/student", { headers })
      .subscribe({
        next: (data) => {
          this.fetchedVideos = data;
          this.loading = false;
        },
        error: (err) => {
          console.error("❌ Failed to fetch videos:", err);
          this.errorMsg = err.error || "❌ Failed to fetch videos";
          this.loading = false;
        }
      });
  }
}
