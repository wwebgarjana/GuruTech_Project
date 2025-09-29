import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';

@Component({
  selector: 'app-my-batches',
  standalone: true,
  templateUrl: './my-batches.html',
  styleUrls: ['./my-batches.css']
})
export class MyBatches implements OnInit {
  
  profile: any = {}; // store trainer profile
  totalStudents: number = 0;

  constructor(private http: HttpClient) {}

  ngOnInit(): void {
    this.loadTrainerProfile();
  }

  // 🔹 Load trainer profile
  loadTrainerProfile() {
    const email = localStorage.getItem("userEmail");

    if (email) {
      const apiUrl = `http://localhost:8082/api/trainers/profile?email=${encodeURIComponent(email)}`;

      this.http.get(apiUrl).subscribe({
        next: (res: any) => {
          console.log("✅ API Response:", res);

          this.profile = res;

          // fallback values
          this.profile.courses = this.profile.courses || "No courses assigned yet.";
          this.profile.batches = this.profile.batches || "No batches assigned yet.";
          this.profile.startDate = this.profile.startDate || "N/A";
          this.profile.endDate = this.profile.endDate || "N/A";

          // 🔹 Fetch total students for this trainer (after trainerId is available)
          if (this.profile.trainerId) {
            this.http.get<number>(`http://localhost:8082/assign/count/${this.profile.trainerId}`)
              .subscribe({
                next: (count) => {
                  this.totalStudents = count;
                },
                error: (err) => {
                  console.error("❌ Error fetching total students:", err);
                }
              });
          }
        },
        error: (err) => {
          console.error("❌ API Error:", err);
        }
      });
    } else {
      console.warn("⚠️ No trainerEmail found in localStorage. Please login again.");
      // this.router.navigate(['/login']);
    }
  }
}
