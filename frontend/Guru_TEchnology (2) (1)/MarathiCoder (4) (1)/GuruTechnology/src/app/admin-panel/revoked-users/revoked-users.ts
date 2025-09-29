import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { CommonModule } from '@angular/common';

interface RevokedUser {
  id: number;
  originalId: string;
  email: string;
  role: string;
  revokedBy: string;
  reason: string;
  revokedOn: Date | null;  // ✅ match mapping
}


@Component({
  selector: 'app-revoked-users',
  standalone: true,
  imports: [CommonModule],          // ✅ Required for *ngIf and *ngFor
  templateUrl: './revoked-users.html',
  styleUrls: ['./revoked-users.css']
})
export class RevokedUsers implements OnInit {
  revokedUsers: RevokedUser[] = [];
  loading = true;
  errorMsg = '';

  constructor(private http: HttpClient) {}

  ngOnInit(): void {
    this.http.get<RevokedUser[]>('http://localhost:8082/api/revoked-users')
      .subscribe({
        next: data => {
          // Map revokedAt from API to revokedOn as Date
          this.revokedUsers = data.map(user => ({
  ...user,
  // Use the correct field from backend
  revokedOn: user.revokedOn ? new Date(user.revokedOn) : null
}));


          this.loading = false;
        },
        error: err => {
          console.error(err);
          this.errorMsg = 'Failed to load revoked users';
          this.loading = false;
        }
      });
  }
}
