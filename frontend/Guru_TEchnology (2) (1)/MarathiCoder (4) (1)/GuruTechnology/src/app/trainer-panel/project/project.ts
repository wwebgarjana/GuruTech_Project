







import { Component, ViewChild, OnInit } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import {
  FormBuilder,
  FormGroup,
  Validators,
  ReactiveFormsModule,
  FormsModule
} from '@angular/forms';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-project',
  standalone: true,
  imports: [CommonModule, FormsModule, ReactiveFormsModule],
  templateUrl: './project.html',
  styleUrls: ['./project.css']
})
export class Project implements OnInit {
  // Form for trainer
  projectForm: FormGroup;
  @ViewChild('fileInput') fileInput!: { nativeElement: HTMLInputElement };
  selectedFile: File | null = null;

  // Submitted projects (student view)
  submittedProjects: any[] = [];
  assignedBatches: string[] = [];
  assignedCourses: string[] = [];
  // Feedback message
  message: string = '';
  today: string = ''; 
  private baseUrl = 'http://localhost:8082/api/projects';

  constructor(private fb: FormBuilder, private http: HttpClient) {
    this.projectForm = this.fb.group({
      batch: ['', Validators.required],
      course: ['', Validators.required],
      description: ['', Validators.required],
      techStack: [''],
      assignedDate: ['', Validators.required]
    });
  }

  ngOnInit(): void {
        this.today = new Date().toISOString().split('T')[0]; 
    this.fetchSubmittedProjects();
        this.fetchTrainerAssignments();
  }

  /* ---------------- Trainer: file change ---------------- */
  onFileChange(event: Event): void {
    const input = event.target as HTMLInputElement;
    if (input.files && input.files.length > 0) {
      this.selectedFile = input.files[0];
    } else {
      this.selectedFile = null;
    }
  }

  /* ---------------- Trainer: upload / assign project ---------------- */
  submitProject(): void {
    const token = localStorage.getItem('token');
    if (!token) {
      this.message = '❌ Unauthorized! Please login as trainer.';
      alert(this.message);
      return;
    }

    if (this.projectForm.invalid || !this.selectedFile) {
      this.message = '⚠ All required fields must be filled and a file selected!';
      alert(this.message);
      return;
    }

    const formData = new FormData();
    formData.append('batch', this.projectForm.get('batch')?.value);
    formData.append('course', this.projectForm.get('course')?.value);
    formData.append('description', this.projectForm.get('description')?.value);
    formData.append('techStack', this.projectForm.get('techStack')?.value || '');
    formData.append('assignedDate', this.projectForm.get('assignedDate')?.value);
    formData.append('file', this.selectedFile as Blob);
console.log('Selected file:', this.selectedFile);
console.log('Form values:', this.projectForm.value);

    const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);

    this.http.post<{ message?: string }>(`${this.baseUrl}/upload`, formData, { headers })
      .subscribe({
        next: (res) => {
          this.message = res.message || '✅ Project uploaded successfully!';
          alert(this.message);
          this.resetForm();
          this.fetchSubmittedProjects();
        },
        error: (err) => {
          console.error('❌ Project upload error:', err);
          this.message = '❌ Project upload failed!';
          alert(this.message);
        }
      });
  }

  /* ---------------- Reset form ---------------- */
  resetForm(): void {
    this.projectForm.reset();
    this.selectedFile = null;
    if (this.fileInput && this.fileInput.nativeElement) {
      this.fileInput.nativeElement.value = '';
    }
  }

  /* ---------------- Student: fetch submitted projects ---------------- */
  fetchSubmittedProjects(): void {
    this.http.get<any[]>(`${this.baseUrl}/submitted/all`).subscribe({
      next: (data) => {
        this.submittedProjects = data.map(p => ({
          ...p,
          submissionFileUrl: p.submissionFileName
            ? `${this.baseUrl}/${p.id}/download/submission`
            : null
        }));
      },
      error: (err) => {
        console.error('Error fetching submitted projects:', err);
      }
    });
  }

  fetchTrainerAssignments(): void {
  const trainerEmail = localStorage.getItem("userEmail"); // ✅ always from localStorage

  if (trainerEmail) {
    const apiUrl = `http://localhost:8082/api/trainers/email/${encodeURIComponent(trainerEmail)}/assignments`;

    this.http.get<any>(apiUrl).subscribe({
      next: (res) => {
        console.log("✅ Assignments API Response:", res);

        // Convert to string[] explicitly (fixes unknown[] error)
        this.assignedBatches = res.assignments
          ? Array.from(new Set(res.assignments.map((a: any) => String(a.batch))))
          : [];

        this.assignedCourses = res.assignments
          ? Array.from(new Set(res.assignments.map((a: any) => String(a.course))))
          : [];

        console.log("📌 Batches:", this.assignedBatches);
        console.log("📌 Courses:", this.assignedCourses);
      },
      error: (err) => {
        console.error("❌ Assignments API Error:", err);
      }
    });
  } else {
    console.warn("⚠️ No trainerEmail found in localStorage. Please login again.");
    // this.router.navigate(['/login']); // optional redirect
  }
}


}


