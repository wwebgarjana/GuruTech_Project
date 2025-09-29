
import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
 
@Component({
  selector: 'app-payment',
  imports:[CommonModule,FormsModule],
  templateUrl: './payment.html',
  styleUrls: ['./payment.css']
})
export class Payment implements OnInit {
 
  students: any[] = [];  
  filteredStudents: any[] = []; 
  apiUrl = 'http://localhost:8082/api/payments';  
  amounts: { [key: string]: number } = {}; 
 
  totalReceived: number = 0;
  totalPending: number = 0;
 
  // ✅ filter variables
  idSearch: string = '';
  courseSearch: string = '';
 
  statusFilter: string = '';
 
 
  constructor(private http: HttpClient) {}
 
  ngOnInit(): void {
    this.loadPayments();
  }
 
  // ✅ fetch admin payment summary
  loadPayments() {
    this.http.get<any[]>(`${this.apiUrl}/admin/payments`).subscribe({
      next: (res) => {
        this.students = res;
        this.filteredStudents = res; 
 
        // ✅ Total calculation (for summary cards)
        this.totalReceived = this.students
          .filter(s => s.paymentStatus === 'PAID')
          .reduce((sum, s) => sum + (s.amountPaid || 0), 0);
 
        this.totalPending = this.students
          .filter(s => s.paymentStatus !== 'PAID')
          .reduce((sum, s) => sum + (s.amountPaid || 0), 0);
      },
      error: (err) => {
        console.error('Error fetching admin payments:', err);
      }
    });
  }
 
  // ✅ pay API call
  makePayment(studentId: string) {
    const amount = this.amounts[studentId]; 
    this.http.post(`${this.apiUrl}/pay/${studentId}?amount=${amount}`, {})
      .subscribe({
        next: (res: any) => {
          alert("Payment successful!");
          this.loadPayments(); 
        },
        error: (err) => {
          console.error('Payment failed:', err);
          alert('Payment failed!');
        }
      });
  }
 
  // ✅ filter apply function
applyFilters() {
  this.filteredStudents = this.students.filter(s => {
    const idMatch = this.idSearch
      ? s.studentId?.toString().toLowerCase().includes(this.idSearch.toLowerCase())
      : true;
 
    const courseMatch = this.courseSearch
      ? s.course?.toLowerCase() === this.courseSearch.toLowerCase()
      : true;
 
    const statusMatch = this.statusFilter
      ? s.paymentStatus?.toLowerCase() === this.statusFilter.toLowerCase()
      : true;
 
    return idMatch && courseMatch && statusMatch;
  });
}
downloadPayslip(email: string) {
  const url = `${this.apiUrl}/generate/${encodeURIComponent(email)}`;
  this.http.get(url, { responseType: 'blob' }).subscribe({
    next: (blob) => {
      
      const link = document.createElement('a');
      const fileURL = window.URL.createObjectURL(blob);
      link.href = fileURL;
      link.download = 'payslip.pdf';
      link.click();
      window.URL.revokeObjectURL(fileURL);
    },
    error: (err) => {
      console.error('Payslip download failed:', err);
      alert('Failed to download payslip.');
    }
  });
}
sendReceipt(email: string) {
  const url = `${this.apiUrl}/sendReceipt/${encodeURIComponent(email)}`;
  this.http.get(url).subscribe({
    next: () => alert(`Receipt sent to ${email}`),
    error: (err) => {
      console.error('Failed to send receipt', err);
      alert('Failed to send receipt');
    }
  });
}
 
}
 
 