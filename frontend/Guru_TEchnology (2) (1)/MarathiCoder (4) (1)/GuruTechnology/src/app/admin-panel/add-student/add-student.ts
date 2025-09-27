



import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-add-student',
  standalone: true,
  imports: [FormsModule, CommonModule],
  templateUrl: './add-student.html',
  styleUrls: ['./add-student.css']
})
export class AddStudent implements OnInit {
  today: string = '';

  student: any = {
    studentId: '',
    collegeId: '',
    name: '',
    aadhaar: '',
    mobile: '',
    birthdate: '',
    address: '',
    course: '',
    email: '',
    password: ''
  };

  profile: any = null;
  searchEmail: string = '';

  constructor(private http: HttpClient) {}

  ngOnInit(): void {
    const currentDate = new Date();
    this.today = currentDate.toISOString().split('T')[0];

    // 🔹 Fetch last student ID from backend
    this.http.get('http://localhost:8082/api/students/last-id', { responseType: 'text' })
      .subscribe({
        next: (lastIdFromDB: string) => {
          // Example: lastIdFromDB = "ST002"
          const numericPart = parseInt(lastIdFromDB.replace("ST", ""), 10);
          const nextId = numericPart + 1;
          this.student.studentId = 'ST' + nextId.toString().padStart(3, '0');
        },
        error: (err) => {
          console.error("❌ Error fetching last student ID:", err);
          // fallback
          this.student.studentId = 'ST001';
        }
      });

    // Generate password
    this.student.password = crypto.randomUUID?.() || Math.random().toString(36).slice(-8);
  }

  submitForm(form: any): void {
    if (form.invalid || !this.student.birthdate) {
      alert('Please fill all required fields');
      return;
    }

    const studentPayload = { ...this.student };

    this.http.post('http://localhost:8082/api/students/register', studentPayload).subscribe({
      next: () => {
        alert('Student Registered Successfully!');

        const emailPayload = {
          email: studentPayload.email,
          name: studentPayload.name,
          studentId: studentPayload.studentId,
          password: studentPayload.password
        };

        this.http.post('http://localhost:8082/api/students/send-student-email', emailPayload).subscribe({
          next: () => {
            alert('✅ Welcome Email sent successfully!');
            this.resetForm();
          },
          error: err => {
            console.error('❌ Email sending error:', err);
            alert('Email failed to send: ' + err.message);
          }
        });
      },
      error: err => {
  console.error('❌ Registration error:', err);

  // check backend message
  let errorMsg = 'Registration Failed. Please try again.';

  if (err.status === 409) {
    const backendMsg = err.error?.message?.toLowerCase() || '';

    if (backendMsg.includes('email')) {
      errorMsg = 'This email is already registered. Please use another email.';
    } else if (backendMsg.includes('mobile')) {
      errorMsg = 'This mobile number is already registered. Please use another number.';
    } else if (backendMsg.includes('aadhaar')) {
      errorMsg = 'This Aadhaar number is already registered. Please use another Aadhaar.';
    } else {
      errorMsg = 'Duplicate entry found. Please check your details.';
    }
  }

  alert(errorMsg);
}

    });
  }

  resetForm() {
    this.profile = null;
    this.student = {
      studentId: '',
      collegeId: '',
      name: '',
      aadhaar: '',
      mobile: '',
      birthdate: '',
      address: '',
      course: '',
      email: '',
      password: ''
    };

    // 🔹 Re-fetch last student ID from backend after registration
    this.http.get('http://localhost:8082/api/students/last-id', { responseType: 'text' })
      .subscribe({
        next: (lastIdFromDB: string) => {
          const numericPart = parseInt(lastIdFromDB.replace("ST", ""), 10);
          const nextId = numericPart + 1;
          this.student.studentId = 'ST' + nextId.toString().padStart(3, '0');
        },
        error: (err) => {
          console.error("❌ Error fetching last student ID:", err);
          this.student.studentId = 'ST001';
        }
      });

    this.student.password = crypto.randomUUID?.() || Math.random().toString(36).slice(-8);
  }

  fetchProfile(): void {
    const email = this.searchEmail.trim().toLowerCase();
    this.http.get(`http://localhost:8082/api/students/profile?email=${email}`).subscribe({
      next: (data: any) => this.profile = data,
      error: err => {
        alert('Profile not found');
        this.profile = null;
        console.error(err);
      }
    });
  }

  allowOnlyLetters(event: KeyboardEvent): boolean {
    const char = String.fromCharCode(event.keyCode || event.which);
    const pattern = /^[A-Za-z ]+$/;
    if (!pattern.test(char)) {
      event.preventDefault();
      return false;
    }
    return true;
  }

  allowOnlyDigits(event: KeyboardEvent): boolean {
    const char = String.fromCharCode(event.keyCode || event.which);
    const pattern = /^[0-9]+$/;
    if (!pattern.test(char)) {
      event.preventDefault();
      return false;
    }
    return true;
  }
}
