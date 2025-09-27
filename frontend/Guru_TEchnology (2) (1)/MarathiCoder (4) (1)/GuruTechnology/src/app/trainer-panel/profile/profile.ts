import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-profile',
  standalone: true,
  templateUrl: './profile.html',
  styleUrls: ['./profile.css'],
  imports: [CommonModule, FormsModule]
})
export class Profile implements OnInit {
  trainer: any = {
    education: [],
    experience: [],
    batches: []
  };

  loading = true;
  editEducation = false;
  editExperience = false;

  // Degree options
  degreeOptions = [
    'Bachelor of Science (B.Sc)',
    'Bachelor of Arts (B.A)',
    'Bachelor of Commerce (B.Com)',
    'Bachelor of Engineering (B.E)',
    'Bachelor of Technology (B.Tech)',
    'Master of Science (M.Sc)',
    'Master of Arts (M.A)',
    'Master of Commerce (M.Com)',
    'Master of Technology (M.Tech)',
    'Master of Business Administration (MBA)',
    'Other'
  ];

  // Form entries
  newEducation = [{ type: '', customDegree: '' }];
  newExperience: string[] = [];

  constructor(private http: HttpClient) {}

  ngOnInit(): void {
    const email = localStorage.getItem('userEmail');
    if (!email) {
      alert('No user email found. Please login again.');
      return;
    }

    // ✅ If cache exists, show immediately
    const cachedProfile = localStorage.getItem('trainerProfile');
    if (cachedProfile) {
      this.setProfile(JSON.parse(cachedProfile));
      this.loading = false;
    }

    // ✅ Always fetch fresh data from backend
    this.http
      .get(`http://localhost:8082/api/trainers/profile?email=${email}`)
      .subscribe({
        next: (res: any) => {
          const profileData = {
            ...res,
            education: res.education || [],
            experience: res.experience || [],
            batches: Array.isArray(res.batches)
              ? res.batches
              : [
                  {
                    course: res.courses || 'N/A',
                    batch: res.batches || 'N/A'
                  }
                ]
          };
          this.setProfile(profileData);

          // ✅ update cache
          localStorage.setItem('trainerProfile', JSON.stringify(this.trainer));
          this.loading = false;
        },
        error: (err) => {
          console.error('Error loading profile', err);
          this.loading = false;
        }
      });
  }

  // ✅ Helper to set profile data
  private setProfile(data: any) {
    this.trainer = data;
    this.newEducation = (this.trainer.education || []).map((edu: string) => {
      return this.degreeOptions.includes(edu)
        ? { type: edu, customDegree: '' }
        : { type: 'Other', customDegree: edu };
    });
    this.newExperience = [...this.trainer.experience];
  }

  // Handle dropdown change
  onDegreeChange(index: number) {
    if (this.newEducation[index].type !== 'Other') {
      this.newEducation[index].customDegree = '';
    }
    this.saveToLocal();
  }

  // Add new education
  addEducation() {
    this.newEducation.push({ type: '', customDegree: '' });
    this.saveToLocal();
  }

  // Delete education
  deleteEducation(index: number) {
    this.newEducation.splice(index, 1);
    this.saveToLocal();
  }

  // Save unsaved education to localStorage (cache only)
  saveToLocal() {
    localStorage.setItem('unsavedEducation', JSON.stringify(this.newEducation));
  }

  // Save to backend
  saveEducation() {
    this.trainer.education = this.newEducation
      .map(entry =>
        entry.type === 'Other' ? entry.customDegree.trim() : entry.type
      )
      .filter(degree => degree);

    const email = localStorage.getItem('userEmail');

    this.http.post(`http://localhost:8082/api/trainers/update-education`, {
      email,
      education: this.trainer.education
    }).subscribe({
      next: () => {
        this.editEducation = false;
        localStorage.removeItem('unsavedEducation');
        localStorage.setItem('trainerProfile', JSON.stringify(this.trainer)); // ✅ update cache
      },
      error: (err) => console.error('Error saving education', err)
    });
  }

  // ✅ Experience Handling
  addExperience() {
    this.newExperience.push('');
  }

  deleteExperience(index: number) {
    this.newExperience.splice(index, 1);
  }

  saveExperience() {
    this.trainer.experience = [...this.newExperience];
    const email = localStorage.getItem('userEmail');

    this.http.post(`http://localhost:8082/api/trainers/update-experience`, {
      email,
      experience: this.trainer.experience
    }).subscribe({
      next: () => {
        this.editExperience = false;
        localStorage.setItem('trainerProfile', JSON.stringify(this.trainer)); // ✅ update cache
      },
      error: (err) => console.error('Error saving experience', err)
    });
  }

  // ✅ TrackBy to fix cursor reset issue
  trackByIndex(index: number, obj: any): any {
    return index;
  }
}
