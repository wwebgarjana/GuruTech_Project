


import { Component } from '@angular/core';
import { Router, RouterModule } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { HttpClient, HttpHeaders } from '@angular/common/http';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [FormsModule, RouterModule],
  templateUrl: './login.html',
  styleUrls: ['./login.css']
})
export class Login {
  email: string = '';
  password: string = '';

  constructor(private router: Router, private http: HttpClient) {}

  login(event: Event): void {
    event.preventDefault();

    const headers = new HttpHeaders({ 'Content-Type': 'application/json' });
    const body = { email: this.email, password: this.password };

    this.http.post<any>('http://localhost:8082/api/auth/login', body, { headers }).subscribe({
      next: (res) => {
        console.log('Login successful', res);

        localStorage.setItem('token', res.token);
        localStorage.setItem('role', res.role);
        localStorage.setItem('userEmail', this.email);

        if(res.studentId) localStorage.setItem('studentId', res.studentId);
        if(res.batch) localStorage.setItem('userBatch', res.batch);

        // First login → reset password
        if (res.firstLogin) {
          this.router.navigate(['/reset-password']);
          return;
        }

        // Role-based routing
        if (res.role === 'admin') this.router.navigate(['/admin-panel/dashboard']);
        else if (res.role === 'trainer') this.router.navigate(['/trainer-panel/dashboard']);
        else if (res.role === 'student') this.router.navigate(['/student-panel/dashboard']);
      },
      error: (err) => {
        console.error('Login failed', err);
        alert('Invalid email or password');
      }
    });
  }
}
