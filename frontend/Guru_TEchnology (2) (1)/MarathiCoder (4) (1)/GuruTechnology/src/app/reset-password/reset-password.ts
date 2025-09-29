


import { Component } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Router } from '@angular/router';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-reset-password',
  standalone: true,
  imports: [FormsModule],
  templateUrl: './reset-password.html',
  styleUrls: ['./reset-password.css']
})
export class ResetPassword {
  newPassword: string = '';
  confirmPassword: string = '';
  email: string = localStorage.getItem('userEmail') || '';

  constructor(private http: HttpClient, private router: Router) {}

  resetPassword(): void {
  if (this.newPassword !== this.confirmPassword) {
    alert('Passwords do not match');
    return;
  }

  const email = this.email;
  console.log('Resetting password for:', email);

  const headers = new HttpHeaders({ 'Content-Type': 'application/json' });
  const body = { email, newPassword: this.newPassword };

  this.http.put('http://localhost:8082/api/auth/reset-password', body, { headers })
    .subscribe({
      next: (res) => {
        console.log('Reset success', res);
        alert('Password reset successful');
        const role = localStorage.getItem('role');
        if(role === 'admin') this.router.navigate(['/admin-panel/dashboard']);
        else if(role === 'trainer') this.router.navigate(['/trainer-panel/dashboard']);
        else if(role === 'student') this.router.navigate(['/student-panel/dashboard']);
        else this.router.navigate(['/login']);
      },
      error: (err) => {
        console.error('Reset password failed', err);
        alert('Failed to reset password: ' + err.message);
      }
    });
}

}
