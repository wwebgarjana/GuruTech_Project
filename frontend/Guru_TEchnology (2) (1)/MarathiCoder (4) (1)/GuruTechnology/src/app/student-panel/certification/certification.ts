import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { CommonModule } from '@angular/common';
 
@Component({
  selector: 'app-certification',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './certification.html',
  styleUrls: ['./certification.css']
})
export class Certification implements OnInit {
 
  certificates: any[] = [];
  message: string = '';
 
  private baseUrl = 'http://localhost:8082/api/students';
 
  constructor(private http: HttpClient) { }
 
  ngOnInit(): void {
    this.fetchCertificates();
  }
 
  fetchCertificates() {
    // Get logged-in student's email from localStorage or auth system
    const loggedInEmail = localStorage.getItem('userEmail');
    if(!loggedInEmail) {
      this.message = 'User not logged in';
      return;
    }
 
    // Use the existing backend API: certificate-summary
    this.http.get<any[]>(`${this.baseUrl}/certificate-summary`).subscribe(
      res => {
        // Filter only the logged-in student's certificate
        this.certificates = res
          .filter(c => c.email === loggedInEmail && c.hasCertificate)
          .map(c => ({
            course: c.course,
            issueDate: new Date().toLocaleDateString(), // you can save real issue date in DB
            certificateUrl: `${this.baseUrl}/download-certificate/${c.studentId}`
          }));
      },
      err => {
        console.error('Error fetching certificates:', err);
        this.message = 'Failed to fetch certificates';
      }
    );
  }
 
  downloadCertificate(cert: any) {
    window.open(cert.certificateUrl, '_blank');
  }
}
 
 