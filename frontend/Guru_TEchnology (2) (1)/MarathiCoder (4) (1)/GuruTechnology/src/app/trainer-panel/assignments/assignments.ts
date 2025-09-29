


import { Component, OnInit, ViewChild } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule, FormsModule, AbstractControl, ValidationErrors } from '@angular/forms';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-assignments',
  standalone: true,
  imports: [CommonModule, FormsModule, ReactiveFormsModule],
  templateUrl: './assignments.html',
  styleUrls: ['./assignments.css']
})
export class Assignments implements OnInit {
  assignmentForm: FormGroup;
  selectedFile!: File;
  assignedBatches: string[] = [];
assignedCourses: string[] = [];
minDate: string = '';

  @ViewChild('fileInput') fileInput: any;

  constructor(private fb: FormBuilder, private http: HttpClient) {
   this.assignmentForm = this.fb.group({
  topic: ['', Validators.required],
  description: ['', Validators.required],
  dueDate: ['', [Validators.required, this.futureDateValidator]],
  batch: ['', Validators.required],
  course: ['', Validators.required]
});

  }

 ngOnInit(): void {
  this.fetchTrainerAssignments();

  const today = new Date();
  const yyyy = today.getFullYear();
  const mm = String(today.getMonth() + 1).padStart(2, '0');
  const dd = String(today.getDate()).padStart(2, '0');
  this.minDate = `${yyyy}-${mm}-${dd}`; // This is for the <input type="date">
}


  // ✅ Custom validator for dueDate (must be today or future)
  futureDateValidator(control: AbstractControl): ValidationErrors | null {
    if (!control.value) return null;

    const today = new Date();
    today.setHours(0, 0, 0, 0); // Remove time
    const selectedDate = new Date(control.value);

    return selectedDate >= today ? null : { invalidDate: true };
  }

  // File selected
  onFileChange(event: any) {
    this.selectedFile = event.target.files[0];
  }

  // Upload assignment
  submitAssignment() {
    if (this.assignmentForm.invalid || !this.selectedFile) {
      alert('⚠ All fields are required and Due Date must be today or a future date!');
      return;
    }

    const formData = new FormData();
formData.append('topic', this.assignmentForm.get('topic')?.value);
formData.append('description', this.assignmentForm.get('description')?.value);
formData.append('dueDate', this.assignmentForm.get('dueDate')?.value);
formData.append('batch', this.assignmentForm.get('batch')?.value);
formData.append('course', this.assignmentForm.get('course')?.value);
formData.append('file', this.selectedFile);

    const token = localStorage.getItem('token');
    if (!token) {
      alert("❌ Unauthorized! Please login as trainer.");
      return;
    }
    const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);

    this.http.post('http://localhost:8082/api/assignments/upload', formData, { headers })
      .subscribe({
        next: () => {
          alert('✅ Assignment uploaded successfully!');
          this.resetForm();
        },
        error: (err) => {
          console.error('❌ Upload error:', err);
          alert('❌ Assignment upload failed!');
        }
      });
  }

  // Reset form
  resetForm() {
    this.assignmentForm.reset();
    this.selectedFile = null!;
    if (this.fileInput) this.fileInput.nativeElement.value = '';
  }
  // Fetch batches and courses for the logged-in trainer
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
      error: (err) => {
        console.error("❌ Assignments API Error:", err);
      }
    });
  }
}
}



