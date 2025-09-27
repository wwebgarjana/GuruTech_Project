

import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-attendance',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './attendance.html',
  styleUrls: ['./attendance.css']
})
export class Attendance implements OnInit {
  attendanceList: any[] = [];
  filteredAttendance: any[] = [];

  // 🔍 Filters
  searchDate: string = '';
  searchStatus: string = '';

  userEmail: string | null = '';

  constructor(private http: HttpClient) {}

  ngOnInit(): void {
    // ✅ localStorage se email nikal lo
    this.userEmail = localStorage.getItem('userEmail');

    if (this.userEmail) {
      const encodedEmail = encodeURIComponent(this.userEmail);

      this.http.get<any[]>(`http://localhost:8082/attendance/student/email/${encodedEmail}`)
        .subscribe({
          next: (res) => {
            console.log('Attendance fetched:', res);
            this.attendanceList = res;
            this.filteredAttendance = res; // 🔥 filteredAttendance me copy
          },
          error: (err) => {
            console.error('Failed to fetch attendance', err);
          }
        });
    } else {
      console.warn('No email found in localStorage');
    }
  }

  // ✅ Filter apply
  applyFilters() {
    this.filteredAttendance = this.attendanceList.filter(record => {
      const matchesDate = this.searchDate ? record.date === this.searchDate : true;
      const matchesStatus = this.searchStatus ? record.status.toLowerCase() === this.searchStatus.toLowerCase() : true;
      return matchesDate && matchesStatus;
    });
  }

  // ✅ Reset filters
  resetFilters() {
    this.searchDate = '';
    this.searchStatus = '';
    this.filteredAttendance = [...this.attendanceList];
  }
}
