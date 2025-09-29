// import { Component, OnInit } from '@angular/core';
// import { HttpClient } from '@angular/common/http';
// import { FormsModule } from '@angular/forms';
// import { CommonModule } from '@angular/common';

// @Component({
//   selector: 'app-schedule',
//   standalone: true,
//   templateUrl: './schedule.html',
//   styleUrls: ['./schedule.css'],
//   imports: [FormsModule, CommonModule]
// })
// export class Schedule implements OnInit {

//   schedule: any = {
//     course: '',
//     topicName: '',
//     startTime: '',
//     endTime: '',
//     date: '',
//     batch: ''
//   };

//   message: string = '';
//   assignedBatches: string[] = [];
//   assignedCourses: string[] = [];
//   constructor(private http: HttpClient) {}

//   ngOnInit(): void {
//      this.fetchTrainerAssignments();
//   }




// addSchedule() {
//   const token = localStorage.getItem("token");
//   if (!token) {
//     this.message = "❌ Please login first!";
//     return;
//   }

//   if (!this.schedule.courseName || !this.schedule.topicName || !this.schedule.startTime || !this.schedule.endTime || !this.schedule.date) {
//     this.message = "❌ Please fill all fields!";
//     return;
//   }

//   // ✅ Start Time < End Time validation
//   const start = this.schedule.startTime;
//   const end = this.schedule.endTime;

//   if (start >= end) {
//     this.message = "❌ End Time must be after Start Time!";
//     return;
//   }

//   // ✅ Date validation: must be today or future
//   const selectedDate = new Date(this.schedule.date);
//   const today = new Date();
//   today.setHours(0,0,0,0); // ignore time part
//   if (selectedDate < today) {
//     this.message = "❌ Date must be today or a future date!";
//     return;
//   }

//   // request body
//   const scheduleRequest = {
//     course: this.schedule.course,
//     topicName: this.schedule.topicName,
//     startTime: this.schedule.startTime,
//     endTime: this.schedule.endTime,
//     date: this.schedule.date,
//     batch: this.schedule.batch
//   };

//   this.http.post("http://localhost:8082/api/schedules/create", scheduleRequest, {
//     headers: { Authorization: `Bearer ${token}` },
//     responseType: 'text'  // ✅ treat response as plain text
//   }).subscribe({
//     next: (res: any) => {
//       console.log("✅ Schedule Created:", res);
//       this.message = res; // show backend string directly
//       this.schedule = { courseName: '', topicName: '', startTime: '', endTime: '', date: '', batch: '' };
//     },
//     error: (err) => {
//       console.error("❌ Error creating schedule:", err);
//       this.message = "❌ Failed to create schedule!";
//     }
//   });
// }

//  fetchTrainerAssignments(): void {
//     const trainerEmail = localStorage.getItem("userEmail");

//     if (trainerEmail) {
//       const apiUrl = `http://localhost:8082/api/trainers/email/${encodeURIComponent(trainerEmail)}/assignments`;

//       this.http.get<any>(apiUrl).subscribe({
//         next: (res) => {
//           this.assignedBatches = res.assignments
//             ? Array.from(new Set(res.assignments.map((a: any) => String(a.batch))))
//             : [];
//           this.assignedCourses = res.assignments
//             ? Array.from(new Set(res.assignments.map((a: any) => String(a.course))))
//             : [];
//           console.log("📌 Batches:", this.assignedBatches);
//           console.log("📌 Courses:", this.assignedCourses);
//         },
//         error: (err) => {
//           console.error("❌ Assignments API Error:", err);
//         }
//       });
//     } else {
//       console.warn("⚠️ No trainerEmail found in localStorage. Please login again.");
//     }
//   }

// }








import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-schedule',
  standalone: true,
  templateUrl: './schedule.html',
  styleUrls: ['./schedule.css'],
  imports: [FormsModule, CommonModule]
})
export class Schedule implements OnInit {

  schedule: any = {
    courseName: '',
    topicName: '',
    meetLink: '',       // ✅ New field for Google Meet
    startTime: '',
    endTime: '',
    date: '',
    batch: ''
  };
today: string = '';
  message: string = '';
  assignedBatches: string[] = [];
  assignedCourses: string[] = [];

  constructor(private http: HttpClient) {}

  ngOnInit(): void {
    this.fetchTrainerAssignments();
    const now = new Date();
    this.today = now.toISOString().split('T')[0];
  }

  addSchedule() {
    const token = localStorage.getItem("token");
    if (!token) {
      this.message = "❌ Please login first!";
      return;
    }

    if (!this.schedule.courseName || !this.schedule.topicName || !this.schedule.startTime || !this.schedule.endTime || !this.schedule.date) {
      this.message = "❌ Please fill all fields!";
      return;
    }

    // ✅ Start Time < End Time validation
    const start = this.schedule.startTime;
    const end = this.schedule.endTime;

    if (start >= end) {
      this.message = "❌ End Time must be after Start Time!";
      return;
    }

    // ✅ Date validation: must be today or future
    const selectedDate = new Date(this.schedule.date);
    const today = new Date();
    today.setHours(0,0,0,0); // ignore time part
    if (selectedDate < today) {
      this.message = "❌ Date must be today or a future date!";
      return;
    }

    // ✅ Prepare request body including Google Meet link
    const scheduleRequest = {
      course: this.schedule.courseName,
      topicName: this.schedule.topicName,
      meetLink: this.schedule.meetLink,
      startTime: this.schedule.startTime,
      endTime: this.schedule.endTime,
      date: this.schedule.date,
      batch: this.schedule.batch
    };

    this.http.post("http://localhost:8082/api/schedules/create", scheduleRequest, {
      headers: { Authorization: `Bearer ${token}` },
      responseType: 'text'  // treat response as plain text
    }).subscribe({
      next: (res: any) => {
        console.log("✅ Schedule Created:", res);
        this.message = res; // show backend string directly
        // Reset form
        this.schedule = { courseName: '', topicName: '', meetLink: '', startTime: '', endTime: '', date: '', batch: '' };
      },
      error: (err) => {
        console.error("❌ Error creating schedule:", err);
        this.message = "❌ Failed to create schedule!";
      }
    });
  }

  fetchTrainerAssignments(): void {
    const trainerEmail = localStorage.getItem("userEmail");

    if (trainerEmail) {
      const apiUrl = `http://localhost:8082/api/trainers/email/${encodeURIComponent(trainerEmail)}/assignments`;

      this.http.get<any>(apiUrl).subscribe({
        next: (res) => {
          this.assignedBatches = res.assignments
            ? Array.from(new Set(res.assignments.map((a: any) => String(a.batch))))
            : [];
          this.assignedCourses = res.assignments
            ? Array.from(new Set(res.assignments.map((a: any) => String(a.course))))
            : [];
          console.log("📌 Batches:", this.assignedBatches);
          console.log("📌 Courses:", this.assignedCourses);
        },
        error: (err) => {
          console.error("❌ Assignments API Error:", err);
        }
      });
    } else {
      console.warn("⚠️ No trainerEmail found in localStorage. Please login again.");
    }
  }
}
