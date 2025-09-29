import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
 
@Component({
  selector: 'app-job-updates',
  standalone: true,
  imports:[FormsModule,CommonModule],
  templateUrl: './job-updates.html',
  styleUrls: ['./job-updates.css']
})
export class JobUpdates implements OnInit {
  jobs: any[] = [];
  filteredJobs: any[] = [];   // ✅ Search ke liye alag array
  searchText: string = '';    // ✅ User ka input
 
  private baseUrl = 'http://localhost:8082/api/jobs';
 
  constructor(private http: HttpClient) {}
 
  ngOnInit(): void {
    this.getAllJobs();
  }
 
  // ✅ Only Fetch All Jobs
  getAllJobs() {
    this.http.get<any[]>(`${this.baseUrl}/all`).subscribe({
      next: (res) => {
        this.jobs = res;
        this.filteredJobs = res; // ✅ By default sabhi jobs dikhayenge
      },
      error: (err) => {
        console.error('Error fetching jobs', err);
      }
    });
  }
 
  // ✅ Search filter function
  applyFilter() {
    const query = this.searchText.toLowerCase();
    this.filteredJobs = this.jobs.filter(job =>
      job.title.toLowerCase().includes(query) ||
      job.companyName.toLowerCase().includes(query) ||
      job.course.toLowerCase().includes(query) ||
      job.description.toLowerCase().includes(query)
    );
  }
}
 
 