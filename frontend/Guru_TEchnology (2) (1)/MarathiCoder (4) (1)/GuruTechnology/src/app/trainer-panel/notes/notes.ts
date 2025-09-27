

import { Component, ViewChild, OnInit } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { FormsModule, NgForm } from '@angular/forms';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-notes',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './notes.html',
  styleUrls: ['./notes.css']
})
export class Notes implements OnInit {
  noteTitle: string = '';
  noteDesc: string = '';
  selectedFile!: File;
  message: string = '';

  assignedBatches: string[] = [];
  assignedCourses: string[] = [];
  selectedBatch: string = '';
  selectedCourse: string = '';

  @ViewChild('noteForm') noteForm!: NgForm;
  @ViewChild('fileInput') fileInput: any;

  constructor(private http: HttpClient) {}

  ngOnInit(): void {
    this.fetchTrainerAssignments();
  }

  onFileChange(event: any) {
    this.selectedFile = event.target.files[0];
  }

  fetchTrainerAssignments(): void {
    const trainerEmail = localStorage.getItem("userEmail");
    if (!trainerEmail) return;

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
      error: (err) => console.error("Error fetching assignments:", err)
    });
  }

  uploadNote() {
    const token = localStorage.getItem('token');
    if (!token) { alert("❌ Unauthorized!"); return; }

    if (!this.noteTitle || !this.noteDesc || !this.selectedFile || !this.selectedBatch || !this.selectedCourse) {
      alert("⚠ Please fill all fields!");
      return;
    }

    const formData = new FormData();
    formData.append('title', this.noteTitle);
    formData.append('description', this.noteDesc);
    formData.append('file', this.selectedFile);
    formData.append('batch', this.selectedBatch);
    formData.append('course', this.selectedCourse);

    const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);

    this.http.post<{ message: string }>(
      'http://localhost:8082/api/notes/upload',
      formData,
      { headers }
    ).subscribe({
      next: (res) => {
        this.message = res.message || "✅ Note uploaded successfully!";
        alert(this.message);
        this.resetForm();
      },
      error: (err) => {
        console.error("Upload error:", err);
        alert("❌ Failed to upload note!");
      }
    });
  }

  resetForm() {
    this.noteTitle = '';
    this.noteDesc = '';
    this.selectedFile = null!;
    this.selectedBatch = '';
    this.selectedCourse = '';
    if (this.noteForm) this.noteForm.resetForm();
    if (this.fileInput) this.fileInput.nativeElement.value = '';
  }
}
