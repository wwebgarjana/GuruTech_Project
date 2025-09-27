
 
 
 
import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
 
@Component({
  selector: 'app-apply-jobs',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './apply-jobs.html',
  styleUrls: ['./apply-jobs.css']
})
export class ApplyJobs implements OnInit {
 
  jobs: any[] = [];
  email: string | null = '';
  appliedJobIds: number[] = [];
 
  constructor(private http: HttpClient) {}
 
  ngOnInit(): void {
  this.email = localStorage.getItem('userEmail');
 
  if (this.email) {
    this.fetchEligibleJobs(this.email);
    this.fetchAppliedJobs(this.email); // ✅ applied jobs fetch
  } else {
    alert('User email not found! Please login again.');
  }
}
 
  // ✅ Fetch eligible jobs
 
  // ✅ Fetch eligible jobs
  fetchEligibleJobs(email: string): void {
    this.http.get<any[]>(`http://localhost:8082/api/jobs/eligible-jobs/${email}`)
      .subscribe({
        next: (res) => {
          this.jobs = res;
        },
        error: (err) => {
          console.error('Error fetching eligible jobs:', err);
        }
      });
  }
//  ✅ Fetch eligible jobs and applied jobs
 
 
fetchAppliedJobs(email: string): void {
  this.http.get<number[]>(`http://localhost:8082/api/jobs/applied/${email}`)
    .subscribe({
      next: (res) => {
        this.appliedJobIds = res;
      },
      error: (err) => {
        console.error('Error fetching applied jobs:', err);
      }
    });
}
  // ✅ Apply for a job
applyJob(jobId: number): void {
  if (!this.email) return;
 
  this.http.post(`http://localhost:8082/api/jobs/apply/${jobId}`, { email: this.email })
    .subscribe({
      next: () => {
        alert('Applied successfully!');
        this.appliedJobIds.push(jobId); // ✅ update frontend status
      },
      error: (err) => {
        alert(err.error);
      }
    });
  }
 
}
 
 
 
 