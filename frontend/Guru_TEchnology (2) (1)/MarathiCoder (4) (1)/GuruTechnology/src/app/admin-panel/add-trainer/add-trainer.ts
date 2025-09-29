import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { v4 as uuidv4 } from 'uuid';

@Component({
  selector: 'app-add-trainer',
  standalone: true,
  imports: [FormsModule, CommonModule],
  templateUrl: './add-trainer.html',
  styleUrls: ['./add-trainer.css']
})
export class AddTrainer implements OnInit {
  trainer: any = {
    trainerId: '',
    name: '',
    aadhaar: '',
    mobile: '',
    birthdate: '',
    address: '',
    email: '',
    password: '',
    collegeId: '',
    courses: '',
    batches: '',
    startDate: '',
    endDate: ''
  };

  searchEmail: string = '';
  fetchedTrainer: any = null;
  maxDate: string = '';

  constructor(private http: HttpClient) {}

 ngOnInit(): void {
  this.maxDate = new Date().toISOString().split('T')[0];

  // 🔹 Fetch last ID from backend instead of only localStorage
  this.http.get('http://localhost:8082/api/trainers/last-id', { responseType: 'text' }).subscribe({
    next: (lastIdFromDB: string) => {
      // Example lastIdFromDB = "TG002"
      const numericPart = parseInt(lastIdFromDB.replace("TG", ""), 10);
      const nextId = numericPart + 1;

      this.trainer.trainerId = 'TG' + nextId.toString().padStart(3, '0');

      // ✅ Sync localStorage as well, so future form resets remain consistent
      localStorage.setItem('lastTrainerId', nextId.toString());
    },
    error: (err) => {
      console.error("❌ Error fetching last trainer ID:", err);

      // fallback to localStorage if API fails
      const lastId = parseInt(localStorage.getItem('lastTrainerId') || '0', 10);
      this.trainer.trainerId = 'TG' + (lastId + 1).toString().padStart(3, '0');
    }
  });

  // generate random password
  this.trainer.password = uuidv4();
}

  generateTrainerId(): void {
    // Increment lastId in localStorage ONLY after successful registration
    let lastId = parseInt(localStorage.getItem('lastTrainerId') || '0', 10);
    lastId++;
    localStorage.setItem('lastTrainerId', lastId.toString());
    this.trainer.trainerId = 'TG' + lastId.toString().padStart(3, '0');
  }

  submitTrainerForm(form: any): void {
    if (form.invalid || !this.trainer.birthdate) {
      alert('Please fill all required fields');
      return;
    }

    const email = this.trainer.email.trim().toLowerCase();

    // 1️⃣ Check if email already exists
    this.http.get<boolean>(`http://localhost:8082/api/trainers/exists-by-email?email=${email}`).subscribe({
      next: (emailExists) => {
        if (emailExists) {
          alert('Email ID is already registered. Please use a different email.');
          return;
        }

        // 2️⃣ Proceed with registration
        this.trainer.email = email; // normalize email
        this.http.post('http://localhost:8082/api/trainers/register', this.trainer).subscribe({
          next: () => {
            alert('Trainer registered successfully');
            this.sendEmail();
            form.resetForm();

            // ✅ Increment ID in localStorage and prepare next ID for form
            this.generateTrainerId();
            this.trainer.password = uuidv4();
          },
          error: (err) => {
            alert('Either Aadhaar or Mobile number is already in use. Please try another');
            console.error(err);
          }
        });
      },
      error: (err) => {
        alert('Error checking email uniqueness');
        console.error(err);
      }
    });
  }

  sendEmail(): void {
    const emailPayload = {
      email: this.trainer.email,
      name: this.trainer.name,
      trainerId: this.trainer.trainerId,
      password: this.trainer.password
    };

    this.http.post('http://localhost:8082/api/trainers/send-trainer-email', emailPayload, { responseType: 'text' }).subscribe({
      next: () => console.log('Email sent successfully'),
      error: err => console.error('Email failed to send:', err)
    });
  }

  fetchTrainer(): void {
    const email = this.searchEmail.trim().toLowerCase();
    this.http.get(`http://localhost:8082/api/trainers/profile?email=${email}`).subscribe({
      next: (data) => this.fetchedTrainer = data,
      error: () => {
        this.fetchedTrainer = null;
        alert('Trainer not found');
      }
    });
  }

  allowOnlyDigits(event: KeyboardEvent): void {
    if (!/[0-9]/.test(event.key)) event.preventDefault();
  }

  allowOnlyLetters(event: KeyboardEvent): void {
    if (!/[a-zA-Z ]/.test(event.key)) event.preventDefault();
  }
}


