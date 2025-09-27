import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { HttpClient, HttpClientModule } from '@angular/common/http';

@Component({
  selector: 'app-trainers',
  standalone: true,
  templateUrl: './trainers.html',
  styleUrls: ['./trainers.css'],
  imports: [CommonModule, FormsModule, HttpClientModule],
})
export class Trainers implements OnInit {
  searchText: string = '';
  selectedCourse: string = '';
  selectedBatch: string = '';
  selectedStatus: string = '';

  trainers: any[] = [];
  Array: any;

  constructor(private router: Router, private http: HttpClient) {}

  ngOnInit(): void {
    this.fetchTrainers();
  }

  fetchTrainers() {
    this.http.get<any[]>('http://localhost:8082/api/trainers/summary')
      .subscribe({
        next: (data) => {
          this.trainers = data.map(trainer => ({
            ...trainer,
            courses: this.ensureArray(trainer.courses),
            batches: this.ensureArray(trainer.batches)
          }));
        },
        error: (err) => {
          console.error('Error fetching trainers:', err);
        }
      });
  }

  private ensureArray(value: any): string[] {
    if (Array.isArray(value)) return value;
    if (typeof value === 'string')
      return value.split(',').map(v => v.trim()).filter(v => v);
    return [];
  }

  get filteredTrainers() {
    const search = this.searchText.toLowerCase();

    return this.trainers.filter(trainer =>
      (
        trainer.trainerId.toLowerCase().includes(search) ||
        trainer.name.toLowerCase().includes(search) ||
        trainer.courses.some((course: string) =>
          course.toLowerCase().includes(search)
        )
      ) &&
      (this.selectedCourse === '' || trainer.courses.includes(this.selectedCourse)) &&
      (this.selectedBatch === '' || trainer.batches.includes(this.selectedBatch)) &&
      (this.selectedStatus === '' || trainer.status === this.selectedStatus)
    );
  }

  goToAddTrainer() {
    this.router.navigate(['/admin-panel/dashboard/add-trainer']);
  }

  goToRemoveCourse() {
    this.router.navigate(['/admin-panel/dashboard/remove-course']);
  }

  goToRevokeTrainer() {
    this.router.navigate(['/admin-panel/dashboard/revoke-trainer']);
  }

  /** ✅ New navigation to Revoked Users page */
  goToRevokedUsers() {
    this.router.navigate(['/admin-panel/dashboard/revoked-users']);
  }
}
