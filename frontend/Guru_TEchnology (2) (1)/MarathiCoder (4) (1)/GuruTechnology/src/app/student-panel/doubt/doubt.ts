



import { Component, OnInit } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';

interface ChatMsg {
  sender: 'student' | 'trainer';
  senderName: string;
  receiverName: string;
  message: string;
  sentAt?: string;
}

@Component({
  selector: 'app-doubt-student',
  standalone: true,
  imports: [FormsModule, CommonModule],
  templateUrl: './doubt.html',
  styleUrls: ['./doubt.css']
})
export class Doubt implements OnInit {
  selectedTrainer = '';
  doubtText = '';
  messages: ChatMsg[] = [];
  trainers: string[] = [];

  studentEmail: string = '';

  private base = 'http://localhost:8082/api/chat';

  constructor(private http: HttpClient) {}

  ngOnInit() {
    // ✅ Get logged-in user email
    const email = localStorage.getItem('userEmail');

    if (!email) {
      alert('User not logged in!');
      return;
    }

    this.studentEmail = email;
    console.log('Logged-in user email:', this.studentEmail);

    // fetch assigned trainers for this student using email
    const params = new HttpParams().set('studentEmail', this.studentEmail);
    this.http.get<string[]>(`${this.base}/assigned/trainers`, { params })
      .subscribe({
        next: (list) => this.trainers = list,
        error: (err) => {
          console.error('Error fetching trainers:', err);
          alert('Failed to load assigned trainers');
        }
      });
  }

  loadChat() {
    if (!this.selectedTrainer) return;

    // load chat using student email
    this.http.get<ChatMsg[]>(`${this.base}/${this.studentEmail}/${this.selectedTrainer}`)
      .subscribe({
        next: (data) => this.messages = data,
        error: (err) => {
          console.error('Error loading chat:', err);
        }
      });
  }

  sendDoubt() {
    if (!this.doubtText.trim() || !this.selectedTrainer) return;

    const msg: ChatMsg = {
      sender: 'student',
      senderName: this.studentEmail,  // use email as senderName
      receiverName: this.selectedTrainer,
      message: this.doubtText
    };

    this.http.post<ChatMsg>(this.base, msg)
      .subscribe({
        next: () => {
          this.doubtText = '';
          this.loadChat(); // reload chat after sending
        },
        error: (err) => {
          console.error('Error sending doubt:', err);
          alert(err?.error || 'Failed to send. Are you assigned to this trainer?');
        }
      });
  }
}

