
import { Component, OnInit, TemplateRef } from '@angular/core';
import { CommonModule, NgIfContext } from '@angular/common';
import { HttpClient } from '@angular/common/http';
import { FormsModule } from '@angular/forms';
 
@Component({
  selector: 'app-profile',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './profile.html',
  styleUrls: ['./profile.css']
})
export class Profile implements OnInit {
  student: any = null;
  errorMsg: string = '';
  section: string = 'student';
 
  profileImageUrl: string | null = null;  // image show करने के लिए
  formEmail: string = '';
  formData: any = {
    qualification: '',
    major: '',
    institute: '',
    university: '',
    year: '',
    cgpa: '',
    type: '',
    certifications: ''
  };
  educationList: any[] = [];
 
 
  qualificationOptions: string[] = [
    'Bachelor of Arts (BA)', 'SSC', 'HSC',
    'Bachelor of Science (BSc)', 'Bachelor of Commerce (BCom)',
    'Bachelor of Computer Applications (BCA)', 'Bachelor of Business Administration (BBA)',
    'Bachelor of Engineering (BE)', 'Bachelor of Technology (BTech)',
    'Bachelor of Architecture (BArch)', 'Bachelor of Pharmacy (BPharm)',
    'Bachelor of Fine Arts (BFA)', 'Bachelor of Design (BDes)',
    'Bachelor of Social Work (BSW)', 'Master of Arts (MA)',
    'Master of Science (MSc)', 'Master of Commerce (MCom)',
    'Master of Computer Applications (MCA)', 'Master of Business Administration (MBA)',
    'Master of Engineering (ME)', 'Master of Technology (MTech)',
    'Master of Architecture (MArch)', 'Master of Pharmacy (MPharm)',
    'Master of Social Work (MSW)', 'Master of Design (MDes)',
    'Doctor of Philosophy (Ph.D.)', 'Diploma',
    'Advanced Diploma', 'Certificate Course', 'Other'
  ];
  notFound: TemplateRef<NgIfContext<any>> | null | undefined;
 
  constructor(private http: HttpClient) {}
 
  ngOnInit(): void {
    const email = localStorage.getItem('userEmail');
    if (email) {
      this.formEmail = email;
 
      // Profile load
      this.http.get(`http://localhost:8082/api/students/profile?email=${email}`)
        .subscribe({
          next: (res: any) => {
            this.student = res;
            this.loadProfileImage(email);
          },
          error: () => this.errorMsg = 'No student profile found!'
        });
 
      this.fetchSavedEducation(email);
    } else {
      this.errorMsg = 'No email found in local storage!';
    }
  }
 
  // ---------------- IMAGE HANDLING ----------------
  onFileSelected(event: any) {
    const file: File = event.target.files[0];
    if (!file) return;
 
    const formData = new FormData();
    formData.append('file', file);
 
    this.http.post(`http://localhost:8082/api/students/upload-image/${this.formEmail}`, formData, { responseType: 'text' })
      .subscribe({
        next: () => this.loadProfileImage(this.formEmail),
        error: (err) => console.error('Upload error:', err)
      });
  }
 
  loadProfileImage(email: string) {
    this.http.get(`http://localhost:8082/api/students/image/${email}`, { responseType: 'blob' })
      .subscribe({
        next: (res) => {
          const reader = new FileReader();
          reader.onload = () => this.profileImageUrl = reader.result as string;
          reader.readAsDataURL(res);
        },
        error: () => this.profileImageUrl = null
      });
  }
 
  // ---------------- Validation ----------------
  allowOnlyDigits(event: KeyboardEvent): void {
    if (!/[0-9]/.test(event.key)) event.preventDefault();
  }
 
  allowOnlyLetters(event: KeyboardEvent): void {
    if (!/[a-zA-Z ]/.test(event.key)) event.preventDefault();
  }
 
  // ---------------- Education ----------------
  goToSection(section: string) { this.section = section; }
 
  handleChange(event: any, field: string) {
    const value = event.target.value;
    if (field === 'cgpa') {
      if (/^\d{0,2}(\.\d{0,2})?$/.test(value)) this.formData[field] = value;
    } else if (field === 'year') {
      if (/^\d{0,4}$/.test(value)) this.formData[field] = value;
    } else if (['major', 'institute', 'university', 'type'].includes(field)) {
      if (/^[A-Za-z\s]*$/.test(value)) this.formData[field] = value;
    } else {
      this.formData[field] = value;
    }
  }
 
  fetchSavedEducation(email: string) {
    this.http.get<any>(`http://localhost:8082/api/education/get/${email}`)
      .subscribe({
        next: (data) => this.educationList = data.educationList || [],
        error: (err) => console.error('Error fetching education:', err)
      });
  }
 
  saveEducationToDB(entry: any) {
    const payload = { email: this.formEmail, educationList: [entry] };
    this.http.post('http://localhost:8082/api/education/save', payload, { responseType: 'text' })
      .subscribe({
        next: (msg) => {
          alert(msg);
          this.formData = { qualification: '', major: '', institute: '', university: '', year: '', cgpa: '', type: '', certifications: '' };
          this.fetchSavedEducation(this.formEmail);
        },
        error: (err) => alert('Failed to save: ' + err.message)
      });
  }
 
  handleSubmit() {
    if (!this.formEmail) return alert('Please enter your email first.');
    const exists = this.educationList.some(
      (edu) => edu.qualification?.toLowerCase() === this.formData.qualification?.toLowerCase()
    );
    if (exists) return alert(`${this.formData.qualification} already saved!`);
    this.saveEducationToDB(this.formData);
  }
 
  handleAddMore() {
    if (!this.formEmail) return alert('Please enter your email first.');
    const isFilled = Object.values(this.formData).some(val => (val + '').trim() !== '');
    if (!isFilled) return alert('Please fill at least one field before adding.');
    const exists = this.educationList.some(
      (edu) => edu.qualification?.toLowerCase() === this.formData.qualification?.toLowerCase()
    );
    if (exists) return alert(`${this.formData.qualification} already saved!`);
    this.saveEducationToDB(this.formData);
  }
}
 
 