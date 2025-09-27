import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { HttpClient } from '@angular/common/http';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
 
@Component({
  selector: 'app-students',
  standalone: true,
  imports: [RouterModule, FormsModule, CommonModule],
  templateUrl: './students.html',
  styleUrls: ['./students.css']
})
export class Students implements OnInit {
 
  students: any[] = [];         
  filteredStudents: any[] = []; 
 
  searchText: string = '';
  filterCourse: string = '';
  filterBatch: string = '';
  filterStatus: string = '';
 
  constructor(private router: Router, private http: HttpClient) {}
 
  ngOnInit(): void {
    this.loadStudents();
  }
 
  goToAddStudents() {
    this.router.navigate(['/admin-panel/dashboard/add-student']);
  }
 
  goToRevokeStudents() {
    this.router.navigate(['/admin-panel/dashboard/revoke-student']);
  }
 
  

  loadStudents() {
  this.http.get<any[]>('http://localhost:8082/api/students/all')
    .subscribe({
      next: (data) => {
        console.log("All students from backend:", data);
        this.students = data;
        this.filteredStudents = data;
      },
      error: (err) => {
        console.error('Error fetching students:', err);
      }
    });
}

 
  applyFilters() {
    this.filteredStudents = this.students.filter(student => {
      return (
        (!this.searchText || student.name.toLowerCase().includes(this.searchText.toLowerCase())) &&
        (!this.filterCourse || student.course === this.filterCourse) &&
        (!this.filterBatch || student.batch === this.filterBatch) &&
        (!this.filterStatus || student.status === this.filterStatus)
      );
    });
  }
}
 
 