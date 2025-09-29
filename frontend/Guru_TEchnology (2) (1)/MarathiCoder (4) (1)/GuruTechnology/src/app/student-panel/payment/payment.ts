import { Component } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-payment',
  templateUrl: './payment.html',
  imports: [CommonModule, FormsModule],
  styleUrls: ['./payment.css']
})
export class Payment {

  apiUrl = 'http://localhost:8082/api/payments';  // backend base url
  message: string = '';        // <h1> message
  loggedInEmail: string = '';  // store logged-in user's email
  studentEmail: string = '';   // input field email

  constructor(private http: HttpClient) {
    this.loggedInEmail = localStorage.getItem('userEmail') || '';
  }

  // ✅ generate & download payslip
  generatePayslip() {
    if (!this.loggedInEmail) {
      this.message = "You are not logged in!";
      return;
    }

    // ✅ Check: input email should match logged-in email
    if (this.studentEmail.trim().toLowerCase() !== this.loggedInEmail.toLowerCase()) {
      this.message = "You cannot download payslip for another email!";
      return; // stop download
    }

    const url = `${this.apiUrl}/generate/${encodeURIComponent(this.loggedInEmail)}`;

    this.http.get(url, { responseType: 'blob' }).subscribe({
      next: (blob) => {
        // download PDF
        const link = document.createElement('a');
        const fileURL = window.URL.createObjectURL(blob);
        link.href = fileURL;
        link.download = 'payslip.pdf';
        link.click();
        window.URL.revokeObjectURL(fileURL);

        this.message = "Payslip downloaded successfully!";
      },
      error: (err) => {
        console.error('Payslip download failed:', err);
        this.message = "Failed to download payslip. Make sure payment is completed.";
      }
    });
  }
}
