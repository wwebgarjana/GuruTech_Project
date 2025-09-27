
import { Component, OnInit } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-stud-submit-assign',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './stud-submit-assign.html',
  styleUrls: ['./stud-submit-assign.css']
})
export class StudSubmitAssign implements OnInit {

  submittedAssignments: any[] = [];
  selectedFile!: File;
  selectedAssignmentId!: number;

  constructor(private http: HttpClient) {}

  ngOnInit(): void {
    this.fetchSubmittedAssignments();
  }

  // --- File selection ---
  fileSelected(event: any) {
    this.selectedFile = event.target.files[0];
  }

  // --- Submit assignment ---
  submitAssignment() {
    if (!this.selectedFile) {
      alert('❌ Please select a file');
      return;
    }
    if (!this.selectedAssignmentId) {
      alert('❌ Please select assignment ID');
      return;
    }

    const formData = new FormData();
    formData.append('assignmentId', this.selectedAssignmentId.toString());

    // ✅ Get student info from localStorage
    const studentId = localStorage.getItem('studentId') || '';
    const studentEmail = localStorage.getItem('studentEmail') || '';
    if (!studentId || !studentEmail) {
      alert('❌ Student info missing. Please login.');
      return;
    }

    formData.append('studentId', studentId);
    formData.append('studentEmail', studentEmail);
    formData.append('file', this.selectedFile);

    const token = localStorage.getItem('token') || '';
    const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);

    this.http.post('http://localhost:8082/api/submitted-assignments/submit', formData, { headers })
      .subscribe({
        next: (res: any) => {
          alert('✅ Assignment submitted successfully');
          this.selectedFile = undefined!;
          this.selectedAssignmentId = undefined!;
          this.fetchSubmittedAssignments(); // refresh list
        },
        error: (err) => {
          console.error('Submission failed', err);
          alert('❌ Submission failed');
        }
      });
  }

  // --- Fetch submitted assignments ---
  fetchSubmittedAssignments(): void {
    const token = localStorage.getItem('token');
    if (!token) {
      alert('❌ Please login first');
      return;
    }

    const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);

    this.http.get<any[]>('http://localhost:8082/api/submitted-assignments/all', { headers })
      .subscribe({
        next: (data) => {
          this.submittedAssignments = data.map(a => ({
            id: a.id,
            topic: a.topic || 'Untitled',
            studentId: a.studentId || '',       // Use backend value
            studentEmail: a.studentEmail || '', // Use backend value
            fileName: a.fileName,
            fileUrl: a.fileUrl ? `http://localhost:8082${a.fileUrl}` : null
          }));
        },
        error: (err) => {
          console.error('Error fetching submitted assignments:', err);
          alert('❌ Failed to load submitted assignments.');
        }
      });
  }

  // --- Download file ---
  downloadFile(url: string, fileName: string) {
    if (!fileName) fileName = 'submission.pdf';

    const token = localStorage.getItem('token');
    if (!token) {
      alert('❌ Please login first');
      return;
    }

    const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);

    this.http.get(url, { headers, responseType: 'blob' }).subscribe({
      next: (blob: Blob) => {
        const downloadLink = document.createElement('a');
        const urlBlob = window.URL.createObjectURL(blob);
        downloadLink.href = urlBlob;
        downloadLink.download = fileName;
        document.body.appendChild(downloadLink);
        downloadLink.click();
        document.body.removeChild(downloadLink);
        window.URL.revokeObjectURL(urlBlob);
      },
      error: (err) => {
        console.error('Download failed', err);
        alert('❌ Failed to download file');
      }
    });
  }
}
