




import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-attendance',
  standalone: true,
  imports: [FormsModule, CommonModule],
  templateUrl: './attendance.html',
  styleUrls: ['./attendance.css']
})
export class Attendance implements OnInit {

  trainerId: string = '';
  batches: string[] = [];
  selectedBatch: string = '';
  selectedDate: string = '';
  students: any[] = [];
  attendanceMap: { [studentId: string]: string } = {}; 
  submittedAttendance: any[] = [];
  attendanceAlreadyDone: boolean = false;
  today: string = '';  // For disabling future dates

  constructor(private http: HttpClient) {}

  ngOnInit(): void {
    // Set today's date for max attribute (disable future dates)
    const now = new Date();
    const month = (now.getMonth() + 1).toString().padStart(2, '0');
    const day = now.getDate().toString().padStart(2, '0');
    const year = now.getFullYear();
    this.today = `${year}-${month}-${day}`;

    const email = localStorage.getItem('userEmail');
    if (email) {
      this.http.get<any>(`http://localhost:8082/api/trainers/profile?email=${email}`)
        .subscribe(res => {
          this.trainerId = res.trainerId;

          this.http.get<any>(`http://localhost:8082/attendance/batches/${this.trainerId}`)
            .subscribe(res => {
              let batchList: string[] = [];

              if (Array.isArray(res)) {
                batchList = res;
              } else if (typeof res === 'string') {
                batchList = res.split(',').map((b: string) => b.trim());
              } else if (res && res.batches) {
                batchList = res.batches;
              }

              // Remove duplicates (ignore case + trim spaces)
              const normalized = batchList.map(b => b.trim().toLowerCase());
              this.batches = batchList.filter(
                (batch, idx) => normalized.indexOf(batch.trim().toLowerCase()) === idx
              );
            });
        });
    }
  }

  onBatchOrDateChange() {
    if (this.selectedBatch && this.selectedDate) {
      this.http.get<any>(`http://localhost:8082/attendance/students/${this.trainerId}/${this.selectedBatch}/${this.selectedDate}`)
        .subscribe(res => {
          this.students = res.students || [];
          this.attendanceAlreadyDone = res.alreadyMarked;

          if (this.attendanceAlreadyDone) {
            // Pre-fill previous attendance for editing
            this.submittedAttendance = this.students;
            this.attendanceMap = {};
            this.students.forEach(s => this.attendanceMap[s.studentId] = s.status || 'present');
          } else {
            // New attendance
            this.attendanceMap = {};
            this.students.forEach(s => this.attendanceMap[s.studentId] = 'present');
            this.submittedAttendance = [];
          }
        });
    }
  }

  markAttendance() {
    if (!this.selectedBatch || !this.selectedDate || this.students.length === 0) return;

    const attendanceList = this.students.map(s => ({
      studentId: s.studentId,
      studentName: s.studentName,
      trainerId: this.trainerId,
      batchName: this.selectedBatch,
      date: this.selectedDate,
      status: this.attendanceMap[s.studentId]  // latest selected value
    }));

    this.http.post<any>(`http://localhost:8082/attendance/mark`, attendanceList)
      .subscribe({
        next: (res) => {
          alert(res.message || 'Attendance updated successfully!');
          // Update submittedAttendance table
          this.submittedAttendance = this.students.map(s => ({
            ...s,
            status: this.attendanceMap[s.studentId]
          }));
          this.attendanceAlreadyDone = true;
        },
        error: (err) => {
          console.error('Error marking attendance:', err);
          alert('Failed to mark attendance!');
        }
      });
  }
}
