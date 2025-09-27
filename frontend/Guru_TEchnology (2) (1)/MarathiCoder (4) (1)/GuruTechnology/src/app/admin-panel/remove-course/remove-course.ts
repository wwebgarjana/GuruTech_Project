

import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { HttpClient, HttpParams } from '@angular/common/http';

@Component({
  selector: 'app-remove-course',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './remove-course.html',
  styleUrls: ['./remove-course.css']
})
export class RemoveCourse  {
removeCourse() {
throw new Error('Method not implemented.');
}

  email: string = '';
  course: string = '';
  batch: string = '';

  message: string = '';
  assignedCourses: any[] = [];
  trainerName: string = '';
  totalAssignments: number = 0;
trainer: any;
selectedAssignment: any = null;

  constructor(private http: HttpClient) {}

  
  // ✅ Assign course+batch (unique batch logic)
assignCourseBatch() {
  if (!this.email || !this.course || !this.batch) {
    alert("Please enter email, course and batch");
    return;
  }

  const params = new HttpParams()
    .set('email', this.email)
    .set('course', this.course)
    .set('batch', this.batch);

  // 🔹 New endpoint with batch uniqueness check
  this.http.post<any>('http://localhost:8082/api/trainers/assignUniqueBatch', {}, { params })
    .subscribe({
      next: (res) => {
        this.message = res.message;
        this.assignedCourses = res.assignedCourses;
        this.trainerName = res.trainerName;
        this.totalAssignments = res.totalAssigned;
      },
      error: (err) => {
        this.message = "Error: " + (err.error || err.message);
      }
    });
}


  // ✅ Get assignments by email
  getAssignments() {
    if (!this.email) {
      alert("Please enter trainer email");
      return;
    }

    this.http.get<any>(`http://localhost:8082/api/trainers/email/${this.email}/assignments`)
      .subscribe({
        next: (res) => {
          this.trainerName = res.trainerName;
          this.totalAssignments = res.totalAssignments;
          this.assignedCourses = res.assignments;
          this.message = "Assignments fetched successfully!";
        },
        error: (err) => {
          this.message = "Error: " + (err.error?.message || err.message);
        }
      });
  }
  removeAssignment(course: string, batch: string) {
  const params = {
    email: this.email,
    course: course,
    batch: batch
  };

  this.http.delete<any>('http://localhost:8082/api/trainers/removeCourseBatch', { params })
    .subscribe({
      next: (res) => {
        this.message = res.message;
        this.getAssignments(); // refresh list
      },
      error: (err) => {
        this.message = "Error: " + (err.error?.message || err.message);
      }
    });
}

}
