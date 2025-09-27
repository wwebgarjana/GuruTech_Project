import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';

@Component({
  selector: 'app-basic-courses',
  templateUrl: './basic-courses.html',
  styleUrls: ['./basic-courses.css'],
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule]
})
export class BasicCourses {
  // ✅ All Courses by Category
  allCourses = {
    basic: [
      { title: 'Certificate in Advance Excel', duration: '3 months', code: 'CAE031' },
      { title: 'Certificate in Computer Application', duration: '3 months', code: 'CCA031' },
      { title: 'Certificate in MS-Office', duration: '3 months', code: 'CMO031' },
      { title: 'Microsoft & Computer Information Technology', duration: '3 months', code: 'MSCIT031' },
      { title: 'Certificate in Computer Application (Advanced)', duration: '6 months', code: 'CCA061' },
      { title: 'Diploma in Computer Application', duration: '6 months', code: 'DCA062' },
      { title: 'Diploma in Computer Teacher Training', duration: '12 months', code: 'CTT121' },
      { title: 'Certificate in Tally Prime with GST', duration: '3 months', code: 'TALLY031' },
      { title: 'Certificate in JAVA', duration: '3 months', code: 'JAV031' },
      { title: 'Certificate in Web Technology', duration: '3 months', code: 'CWT031' },
      { title: 'Diploma in Web Designing', duration: '6 months', code: 'DWD061' },
      { title: 'Programming in Python & MySQL', duration: '6 months', code: 'PPM061' }
    ],
    it: [
      { title: 'Diploma in Computer Application', duration: '6 months', code: 'DCA062' },
      { title: 'Diploma in Web Designing', duration: '6 months', code: 'DWD061' },
      { title: 'Programming in Python & MySQL', duration: '12 months', code: 'PPM061' },
      { title: 'Certificate in JAVA', duration: '12 months', code: 'JAV031' },
      { title: 'Certificate in Web Technology', duration: '12 months', code: 'CWT031' }
    ],
    internship: [
      { title: 'Marketing & Branding', code: 'INT001' },
      { title: 'IT Development', code: 'INT002' },
      { title: 'Web Design',  code: 'INT003' },
      { title: 'Human Resources (HR)',  code: 'INT004' },
      { title: 'Finance & Accounting',  code: 'INT005' },
      { title: 'Business Operations',  code: 'INT006' },
      { title: 'Sales & Business Development',  code: 'INT007' },
      // { title: 'Strategy & Analytics', duration: '', code: 'INT008' }
    ]
  };

  // ✅ Form state
  selectedCategory: string = '';
  selectedCourse: string = '';
  filteredCourses: any[] = [];
  showForm = false;

  // ✅ Toggle Form
  openForm(courseTitle?: string, courseCode?: string) {
    this.showForm = true;
    if (courseTitle && courseCode) {
      this.selectedCourse = `${courseTitle} (${courseCode})`;
    } else {
      this.selectedCourse = '';
    }
  }
  closeForm() {
    this.showForm = false;
    this.selectedCourse = '';
  }

  // ✅ Filter courses when category changes
onCategoryChange() {
  if (this.selectedCategory) {
    this.filteredCourses = this.allCourses[this.selectedCategory as keyof typeof this.allCourses] || [];
  } else {
    this.filteredCourses = [];
  }
}
  // ✅ Smooth Scroll
  scrollToCourse(courseId: string) {
    const element = document.getElementById(courseId);
    if (element) {
      element.scrollIntoView({ behavior: 'smooth', block: 'start' });
    }
  }

  // ✅ Zig-Zag Courses Section (with images, fees, highlights)
  courses = [
    { 
      title: 'Certificate in Advance Excel', 
      duration: '3 months', 
      fees: '₹5,000', 
      code: 'CAE031',
      image: 'excel.png',
      highlights: ['Excel formulas', 'Pivot tables', 'Macros'] 
    },
    { 
      title: 'Basic Computer Course', 
      duration: '2 months', 
      fees: '₹3,000', 
      code: 'BCC015',
      image: 'basic-computer.jpeg',
      highlights: ['Typing skills', 'MS Office', 'File management'] 
    },
    { 
      title: 'Tally with GST', 
      duration: '4 months', 
      fees: '₹6,500', 
      code: 'TLY041',
      image: 'tally.jpeg',
      highlights: ['Accounting basics', 'GST entries', 'Reports'] 
    },
    { 
      title: 'Certificate in Computer Application', 
      duration: '3 months', 
      fees: '₹4,500', 
      code: 'CCA031',
      image: 'cca.png',
      highlights: ['Word', 'Excel', 'PowerPoint'] 
    },
    { 
      title: 'Certificate in MS-Office', 
      duration: '3 months', 
      fees: '₹4,000', 
      code: 'CMO031',
      image: 'ms-office.png',
      highlights: ['Word', 'Excel', 'PowerPoint'] 
    },
    { 
      title: 'Microsoft & Computer Information Technology (MSCIT)', 
      duration: '3 months', 
      fees: '₹4,000', 
      code: 'MSCIT031',
      image: 'mscit.png',
      highlights: ['Computer basics', 'Digital literacy', 'Internet'] 
    },
    { 
      title: 'Certificate in Computer Application (Advanced)', 
      duration: '6 months', 
      fees: '₹7,500', 
      code: 'CCA061',
      image: 'cca-advanced.jpeg',
      highlights: ['Office tools', 'Basic programming', 'Projects'] 
    },
    { 
      title: 'Diploma in Computer Application', 
      duration: '6 months', 
      fees: '₹10,000', 
      code: 'DCA062',
      image: 'dca.jpeg',
      highlights: ['Full IT basics', 'Office tools', 'Mini project'] 
    },
    { 
      title: 'Diploma in Computer Teacher Training', 
      duration: '12 months', 
      fees: '₹15,000', 
      code: 'CTT121',
      image: 'ctt.png',
      highlights: ['Teaching methods', 'Computer syllabus', 'Practical training'] 
    },
    { 
      title: 'Certificate in Tally Prime with GST', 
      duration: '3 months', 
      fees: '₹6,000', 
      code: 'TALLY031',
      image: 'tally-prime.jpg',
      highlights: ['Tally basics', 'GST entries', 'Reports'] 
    }
  ];
}
