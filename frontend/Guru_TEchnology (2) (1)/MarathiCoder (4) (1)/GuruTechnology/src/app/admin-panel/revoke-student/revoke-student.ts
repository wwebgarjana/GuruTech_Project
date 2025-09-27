import { Component } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
 
@Component({
  selector: 'app-revoke-student',
  standalone: true,
  imports: [FormsModule, CommonModule],
  templateUrl: './revoke-student.html',
  styleUrls: ['./revoke-student.css']
})
export class RevokeStudent { 
  studentId: string = '';
  email: string = '';
  message: string = '';
 
  constructor(private http: HttpClient) {}
 
  revokeAccess() {
    if (!this.studentId || !this.email) {
      this.message = 'Please enter both Student ID and Email';
      return;
    }
 
    const payload = {
      studentId: this.studentId,
      email: this.email
    };
 
    this.http.post('http://localhost:8082/revoke-student', payload, { responseType: 'text', withCredentials: true })
      .subscribe({
        next: (res: any) => {
          this.message = res;
          this.studentId = '';
          this.email = '';
        },
        error: (err) => this.message = 'Error: ' + err.message
      });
  }
}
 