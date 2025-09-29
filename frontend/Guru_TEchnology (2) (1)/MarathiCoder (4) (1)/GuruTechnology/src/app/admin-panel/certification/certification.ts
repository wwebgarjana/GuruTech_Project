import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
 
@Component({
  selector: 'app-certification',
  standalone: true,
  imports: [FormsModule, CommonModule],
  templateUrl: './certification.html',
  styleUrls: ['./certification.css']
})
export class Certification implements OnInit {
 
  students: any[] = [];              
  selectedFile: File | null = null;  
  uploadingStudentId: string = '';   
  message: string = '';              
 
  // ✅ Updated baseUrl for StudentController
  private baseUrl = 'http://localhost:8082/api/students';
 
  constructor(private http: HttpClient) {}
 
  ngOnInit(): void {
    this.fetchStudents();
  }
 
  // ✅ Fetch all students with certificate info
  fetchStudents() {
    this.http.get<any[]>(`${this.baseUrl}/certificate-summary`).subscribe(
      res => {
        console.log('Students fetched:', res);
        this.students = res;
      },
      err => {
        console.error('Error fetching students:', err);
        this.message = 'Failed to fetch students';
      }
    );
  }
 
  // File selected from input
  onFileSelected(event: any, studentId: string) {
    if (event.target.files && event.target.files.length > 0) {
      this.selectedFile = event.target.files[0];
      this.uploadingStudentId = studentId;
      this.message = '';
    }
  }
 
  // ✅ Upload certificate for selected student
  uploadCertificate(studentId: string) {
    if (!this.selectedFile) {
      this.message = 'Please select a file';
      return;
    }
 
    const formData = new FormData();
    formData.append('file', this.selectedFile);
 
    // ✅ Updated upload URL to match backend
    this.http.post(`${this.baseUrl}/upload-certificate/${studentId}`, formData, { responseType: 'text' })
      .subscribe(
        res => {
          this.message = res;
          this.fetchStudents();          // Refresh list after upload
          this.selectedFile = null;      // Reset file
        },
        err => {
          console.error('Upload error:', err);
          this.message = 'Error uploading file';
        }
      );
  }
 
  // ✅ Download certificate for student
  downloadCertificate(studentId: string, studentName: string) {
    // ✅ Updated download URL to match backend
    this.http.get(`${this.baseUrl}/download-certificate/${studentId}`, {
      responseType: 'blob',
      observe: 'response'
    }).subscribe(
      (response) => {
        const blob: Blob = response.body as Blob;
 
        if (!blob || blob.size === 0) {
          this.message = 'Certificate not found or corrupted';
          return;
        }
 
        // Extract filename from backend response header
        const contentDisposition = response.headers.get('Content-Disposition');
        let fileName = `${studentName}_certificate.pdf`;
        if (contentDisposition) {
          const match = contentDisposition.match(/filename="?([^"]+)"?/);
          if (match && match[1]) {
            fileName = match[1];
          }
        }
 
        // Trigger download
        const url = window.URL.createObjectURL(blob);
        const a = document.createElement('a');
        a.href = url;
        a.download = fileName;
        a.click();
        window.URL.revokeObjectURL(url);
      },
      (err) => {
        console.error('Download error:', err);
        this.message = 'Error downloading certificate';
      }
    );
  }
}
 
 