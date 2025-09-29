


import { Component, ViewChild } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { CommonModule } from '@angular/common';
import { FormsModule, NgForm } from '@angular/forms';

@Component({
  selector: 'app-videos',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './videos.html',
  styleUrls: ['./videos.css']
})
export class Videos {
  title: string = '';
  description: string = '';
  selectedFile!: File;
  message: string = '';   // ✅ same as Schedule
assignedBatches: string[] = [];
assignedCourses: string[] = [];
batch: string = '';
course: string = '';

  @ViewChild('videoForm') videoForm!: NgForm;
  @ViewChild('fileInput') fileInput: any;

  constructor(private http: HttpClient) {}
ngOnInit(): void {
  this.fetchTrainerAssignments();
}

  // Handle file input
  onFileChange(event: any) {
    this.selectedFile = event.target.files[0];
  }

  // Upload video (Trainer)
  uploadVideo() {
    const token = localStorage.getItem('token');  // ✅ JWT from localStorage
    if (!token) {
      this.message = "❌ Unauthorized! Please login as trainer.";
      alert(this.message);
      return;
    }

    if (!this.title || !this.description || !this.selectedFile || !this.batch || !this.course) {
  this.message = "⚠ Please fill all fields including Batch and Course!";
  alert(this.message);
  return;
}

    const formData = new FormData();
    formData.append('batch', this.batch);
formData.append('course', this.course);
    formData.append('title', this.title);
    formData.append('description', this.description);
    formData.append('videoFile', this.selectedFile);

    const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);

    this.http.post<{ message: string }>(
      'http://localhost:8082/api/videos/upload',
      formData,
      { headers }
    )
    .subscribe({
      next: (res) => {
        console.log("✅ Video Uploaded:", res);
        this.message = "✅ Video uploaded successfully!";
        alert(this.message);   // ✅ show popup success message
        this.resetForm();
      },
      error: (err) => {
        console.error("❌ Upload error:", err);
        this.message = "❌ Failed to upload video!";
        alert(this.message);
      }
    });
  }

  // Reset form
  resetForm() {
    this.title = '';
    this.description = '';
    this.selectedFile = null!;

    if (this.videoForm) {
      this.videoForm.resetForm();
    }
    if (this.fileInput) {
      this.fileInput.nativeElement.value = '';
    }
  }
  fetchTrainerAssignments(): void {
  const trainerEmail = localStorage.getItem("userEmail");
  if (trainerEmail) {
    const apiUrl = `http://localhost:8082/api/trainers/email/${encodeURIComponent(trainerEmail)}/assignments`;
    this.http.get<any>(apiUrl).subscribe({
      next: (res) => {
        this.assignedBatches = res.assignments
          ? Array.from(new Set(res.assignments.map((a: any) => String(a.batch))))
          : [];
        this.assignedCourses = res.assignments
          ? Array.from(new Set(res.assignments.map((a: any) => String(a.course))))
          : [];
      },
      error: (err) => console.error("❌ Assignments API Error:", err)
    });
  }
}

}
