
import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
 
@Component({
  selector: 'app-home',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './home.html',
  styleUrls: ['./home.css']
})
export class Home implements OnInit {
 
  // ========== Assignments ==========
  assignments: any[] = [];
  filteredAssignments: any[] = [];
  loading: boolean = true;
  error: string = '';
  searchTrainerId: string = '';
 
  // ========== Certifications ==========
  certificates: any[] = [];
  message: string = '';
 
  private baseUrl = 'http://localhost:8082/api/students';
 
  constructor(private http: HttpClient) {}
 
  ngOnInit(): void {
    this.fetchAssignments();
    this.fetchCertificates();
  }
 
  // ✅ Fetch Assignments
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
        this.filteredAssignments = data;
        this.loading = false;
      },
      error: (err) => {
        console.error('Error fetching assignments:', err);
        this.error = 'Failed to fetch assignments';
        this.loading = false;
      }
    });
  }
 
  // ✅ Filter by Trainer
  filterByTrainer(): void {
    if (this.searchTrainerId.trim() === '') {
      this.filteredAssignments = this.assignments;
    } else {
      this.filteredAssignments = this.assignments.filter(a =>
        a.trainerId.toLowerCase().includes(this.searchTrainerId.toLowerCase())
      );
    }
  }
 
  onAddAssignment() {
    alert("Add Assignment feature clicked. Show form or modal here.");
  }
 
  onViewDetails() {
    alert("View Details clicked. Redirect to quiz detail or open modal.");
  }
 
  // ✅ Fetch Certificates
  fetchCertificates() {
    const loggedInEmail = localStorage.getItem('userEmail');
    if (!loggedInEmail) {
      this.message = 'User not logged in';
      return;
    }
 
    this.http.get<any[]>(`${this.baseUrl}/certificate-summary`).subscribe(
      res => {
        this.certificates = res
          .filter(c => c.email === loggedInEmail && c.hasCertificate)
          .map(c => ({
            course: c.course,
            issueDate: new Date().toLocaleDateString(),
            certificateUrl: `${this.baseUrl}/download-certificate/${c.studentId}`
          }));
      },
      err => {
        console.error('Error fetching certificates:', err);
        this.message = 'Failed to fetch certificates';
      }
    );
  }
 
  // ✅ Download
  downloadCertificate(cert: any) {
    window.open(cert.certificateUrl, '_blank');
  }
}
 
 