import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
 
@Component({
  selector: 'app-enroll-courses',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './enroll-courses.html',
  styleUrls: ['./enroll-courses.css']
})
export class EnrollCourses implements OnInit {
 
  assignments: any[] = [];
  filteredAssignments: any[] = [];
  loading: boolean = true;
  error: string = '';
  searchTrainerId: string = '';
 
  constructor(private http: HttpClient) {}
 
  ngOnInit(): void {
    this.fetchAssignments();
  }
 
  fetchAssignments(): void {
    const email = localStorage.getItem('userEmail') || '';
    console.log('Fetching assignments for email:', email);
 
    if (!email) {
      this.error = 'No user email found in localStorage';
      this.loading = false;
      return;
    }
 
    const url = `http://localhost:8082/assign/student?email=${encodeURIComponent(email)}`;
    this.http.get<any[]>(url).subscribe({
      next: (data) => {
        console.log('Assignments received from backend:', data);
        this.assignments = data;
        this.filteredAssignments = data; // for filtering
        this.loading = false;
      },
      error: (err) => {
        console.error('Error fetching assignments:', err);
        this.error = 'Failed to fetch assignments';
        this.loading = false;
      }
    });
  }
 
  // ✅ filter by trainerId
  filterByTrainer(): void {
    if (this.searchTrainerId.trim() === '') {
      this.filteredAssignments = this.assignments;
    } else {
      this.filteredAssignments = this.assignments.filter(a =>
        a.trainerId.toLowerCase().includes(this.searchTrainerId.toLowerCase())
      );
    }
  }
}
 
 