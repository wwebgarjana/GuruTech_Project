import { Component } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-revoke-trainer',
  standalone: true,
  imports: [FormsModule, CommonModule],
  templateUrl: './revoke-trainer.html',
  styleUrls: ['./revoke-trainer.css']
})
export class RevokeTrainerComponent {
  trainerId: string = '';
  email: string = '';
  message: string = '';

  constructor(private http: HttpClient) {}

  revokeAccess() {
    if (!this.trainerId || !this.email) {
      this.message = 'Please enter both Trainer ID and Email';
      return;
    }

    const payload = {
      trainerId: this.trainerId,
      email: this.email
    };

    this.http.post('http://localhost:8082/revoke-trainer', payload, { responseType: 'text', withCredentials: true })
      .subscribe({
        next: (res: any) => {
          this.message = res;
          this.trainerId = '';
          this.email = '';
        },
        error: (err) => this.message = 'Error: ' + err.message
      });
  }
}