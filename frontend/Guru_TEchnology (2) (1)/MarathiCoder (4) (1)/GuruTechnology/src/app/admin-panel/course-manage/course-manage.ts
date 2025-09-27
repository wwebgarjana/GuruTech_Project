

import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { HttpClient, HttpClientModule } from '@angular/common/http';
import { Courses } from '../../courses/courses';
 
@Component({
  selector: 'app-course-manage',
  standalone: true,
  imports: [CommonModule, FormsModule, HttpClientModule],
  templateUrl: './course-manage.html',
  styleUrls: ['./course-manage.css']
})
export class CourseManage implements OnInit {
students: any[] = [];  
 trainers: any[] = []; 
selectedStudent: any;
  studentId: string = '';
  studentName: string = '';
  studentEmail: string = '';   

 
  duration: string = '';
  description: string = '';
  assignments: any[] = [];
  searchTrainerId: string = '';
timeIn: string = '';
timeOut: string = '';
timeError: boolean = false;
timeInMin: string = '';


trainerId: string = '';
assignedCourses: string[] = [];
assignedBatches: string[] = [];
batch: string = '';
course: string = '';

 
  constructor(private http: HttpClient) {}
 
  ngOnInit(): void {
    this.loadAssignments();
      this.loadStudents();
      this.loadTrainers();
 this.setCurrentMinTime();
     
  }
 
setCurrentMinTime() {
  const now = new Date();
  const hours = now.getHours().toString().padStart(2, '0');
  const minutes = now.getMinutes().toString().padStart(2, '0');
  this.timeInMin = `${hours}:${minutes}`;   
}
 
updateMinTimeOut() {
  if (this.timeOut && this.timeOut <= this.timeIn) {
    this.timeOut = '';
  }
}
  loadAssignments() {
    this.http.get<any[]>('http://localhost:8082/assign/all').subscribe({
      next: (res) => this.assignments = res,
      error: () => alert('Error fetching assignments')
    });
  }

 
 
 
loadStudents() {
  this.http.get<any[]>('http://localhost:8082/assign/studentsall').subscribe({
    next: (res) => {
      console.log("Students => ", res);  
      this.students = res;
    },
    error: () => alert('Error fetching students')
  });
}
 
onStudentChange() {
  console.log("Selected Student =>", this.selectedStudent); 
  if (this.selectedStudent) {
    this.studentId = this.selectedStudent.studentId;
    this.studentName = this.selectedStudent.name;     
    this.studentEmail = this.selectedStudent.email;   
  }
}
 
 
loadTrainers(){
  this.http.get<any[]>('http://localhost:8082/assign/trainersall').subscribe({
    next: (res) => this.trainers= res,
    error: () => alert('Error fetching students')
  })
     
}
 
  assignTrainer() {
    if (!this.studentId || !this.studentName || !this.studentEmail || !this.trainerId || !this.timeIn || !this.timeOut) {
      alert('Please fill all required fields');
      return;
    }
  const payload = {
    studentId: this.studentId,
    studentName: this.studentName,
    studentEmail: this.studentEmail,
    trainerId: this.trainerId,
    course: this.course,      
    batch: this.batch,       
    timeIn: this.timeIn,
    timeOut: this.timeOut,
    duration: this.duration,
    description: this.description
  };

 
    this.http.post<any>('http://localhost:8082/assign/trainer', payload).subscribe({
      next: (res) => {
        alert('Trainer Assigned Successfully!');
        this.assignments.push(res);
        this.resetForm();
      },
      error: (err) => {
        let msg = 'Error assigning trainer';
        if (err.error) {
          msg = typeof err.error === 'string' ? err.error : err.error.message || msg;
        }
        alert(msg);
      }
    });
  }
 
  resetForm() {
    this.studentId = '';
    this.studentName = '';
    this.studentEmail = '';  
    this.trainerId = '';
    this.timeIn = '';
    this.timeOut = '';
    this.duration = '';
    this.description = '';
  }
 
  get filteredAssignments() {
    if (!this.searchTrainerId) return this.assignments;
    return this.assignments.filter(a =>
      a.trainerId?.toLowerCase().includes(this.searchTrainerId.toLowerCase())
    );
  }
 
validateTime() {
  if (this.timeIn && this.timeOut) {
    const inTime = new Date("1970-01-01T" + this.timeIn + ":00");
    const outTime = new Date("1970-01-01T" + this.timeOut + ":00");
 
    this.timeError = outTime <= inTime;  
  } else {
    this.timeError = false;
  }
}
loadTrainerAssignments() {
  if (!this.trainerId) return;


this.http.get<any>(`http://localhost:8082/api/trainers/id/${this.trainerId}/assignments`)

    .subscribe({
      next: (res) => {
        const assignments = res.assignments || [];
        this.assignedCourses = Array.from(new Set(assignments.map((a: any) => a.course)));
        this.assignedBatches = Array.from(new Set(assignments.map((a: any) => a.batch)));
      },
      error: (err) => {
        console.error("Error fetching trainer assignments:", err);
        this.assignedCourses = [];
        this.assignedBatches = [];
      }
    });
}

}