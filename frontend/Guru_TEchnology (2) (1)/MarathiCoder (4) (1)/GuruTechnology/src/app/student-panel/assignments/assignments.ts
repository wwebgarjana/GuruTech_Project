



import { Component, OnInit } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-assignments-student',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './assignments.html',
  styleUrls: ['./assignments.css']
})
export class Assignments implements OnInit {
  assignments: any[] = [];
  loading = false;
  errorMsg = '';
  selectedFiles: { [assignmentId: number]: File } = {};
  submittedFileMap: { [assignmentId: number]: { url: string, fileName: string } } = {};
  submittedIds: Set<number> = new Set<number>();

  constructor(private http: HttpClient) {}

  ngOnInit(): void {
    this.fetchAssignments();
    this.loadSubmittedAssignments();
  }

  // Fetch trainer uploaded assignments
  fetchAssignments(): void {
    const token = localStorage.getItem('token');
    if (!token) {
      this.errorMsg = '❌ Please login first!';
      return;
    }
    this.loading = true;
    const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);

    this.http.get<any[]>('http://localhost:8082/api/assignments/student', { headers })
      .subscribe({
        next: data => {
          this.assignments = data.map(a => ({
            ...a,
            downloadUrl: `http://localhost:8082/api/assignments/download/${a.id}`
          }));
          this.loading = false;
        },
        error: err => {
          console.error(err);
          this.errorMsg = '❌ Failed to fetch assignments';
          this.loading = false;
        }
      });
  }

  // Load student submitted assignments
  loadSubmittedAssignments(): void {
    const token = localStorage.getItem('token');
    if (!token) return;
    const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);

    this.http.get<any[]>('http://localhost:8082/api/submitted-assignments/all', { headers })
      .subscribe({
        next: submitted => {
          submitted.forEach(s => {
            this.submittedIds.add(s.assignmentId);
            this.submittedFileMap[s.assignmentId] = { 
              url: `http://localhost:8082${s.fileUrl}`, 
              fileName: s.fileName || `submission_${s.assignmentId}` 
            };
          });
        },
        error: err => console.warn('Could not load submitted assignments', err)
      });
  }

  // Handle file selection
  onFileChange(event: any, assignmentId: number): void {
    const file = event.target.files[0];
    if (file) this.selectedFiles[assignmentId] = file;
  }

  // Submit assignment
  submitAssignmentFile(assignmentId: number): void {
    const file = this.selectedFiles[assignmentId];
    if (!file) {
      alert('⚠️ Please select a file before submitting.');
      return;
    }

    const token = localStorage.getItem('token');
    if (!token) {
      alert('❌ Please login first');
      return;
    }

    const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);
    const formData = new FormData();
    formData.append('assignmentId', assignmentId.toString());
    formData.append('studentId', localStorage.getItem('studentId') || '');
    formData.append('studentEmail', localStorage.getItem('email') || '');
    formData.append('file', file);

    this.http.post<any>('http://localhost:8082/api/submitted-assignments/submit', formData, { headers })
      .subscribe({
        next: res => {
          alert('✅ Assignment submitted!');
          this.submittedIds.add(assignmentId);
          this.submittedFileMap[assignmentId] = { 
            url: `http://localhost:8082${res.fileUrl}`, 
            fileName: res.fileName || `submission_${assignmentId}` 
          };
          delete this.selectedFiles[assignmentId];
        },
        error: err => {
          console.error(err);
          alert('❌ Failed to submit assignment');
        }
      });
  }

  // Check if assignment is already submitted
  isSubmitted(assignmentId: number): boolean {
    return this.submittedIds.has(assignmentId);
  }

  // Download file (trainer or student)
  downloadFile(url: string, fileName: string) {
    if (!fileName) {
      alert('❌ File name is missing!');
      return;
    }

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
      error: err => {
        console.error('Download failed', err);
        alert('❌ Failed to download file');
      }
    });
  }
}
