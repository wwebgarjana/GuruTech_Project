



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
  selector: 'app-doubt-trainer',
  standalone: true,
  imports: [FormsModule, CommonModule],
  templateUrl: './doubt.html',
  styleUrls: ['./doubt.css']
})
export class Doubt implements OnInit {
  selectedStudent = '';
  replyText = '';
  messages: ChatMsg[] = [];
  students: string[] = [];

  trainerEmail: string = '';

  private base = 'http://localhost:8082/api/chat';

  constructor(private http: HttpClient) {}

  ngOnInit() {
    // get logged-in trainer email from localStorage
    const email = localStorage.getItem('userEmail');

    if (!email) {
      alert('Trainer not logged in!');
      return;
    }

    this.trainerEmail = email;

    // fetch assigned students
    const params = new HttpParams().set('trainerEmail', this.trainerEmail); // use correct param
    this.http.get<string[]>(`${this.base}/assigned/students`, { params })
      .subscribe({
        next: (list) => this.students = list,
        error: (err) => {
          console.error('Error fetching assigned students:', err);
          alert('Failed to load assigned students');
        }
      });
  }

  loadChat() {
  if (!this.selectedStudent) return;

  // Correct order: studentEmail first, then trainerEmail
  this.http.get<ChatMsg[]>(`${this.base}/${this.selectedStudent}/${this.trainerEmail}`)
    .subscribe({
      next: (data) => this.messages = data,
      error: (err) => {
        console.error('Error loading chat:', err);
      }
    });

  }

  sendReply() {
    if (!this.replyText.trim() || !this.selectedStudent) return;

    const msg: ChatMsg = {
      sender: 'trainer',
      senderName: this.trainerEmail,  // use email as senderName
      receiverName: this.selectedStudent,
      message: this.replyText
    };

    this.http.post<ChatMsg>(this.base, msg)
      .subscribe({
        next: () => {
          this.replyText = '';
          this.loadChat(); // reload chat after sending
        },
        error: (err) => {
          console.error('Error sending reply:', err);
          alert(err?.error || 'Failed to send. Is this student assigned to you?');
        }
      });
  }
}
