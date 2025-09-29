import { Component } from '@angular/core';
import { CommonModule, NgFor, NgIf } from '@angular/common';

@Component({
  selector: 'app-internship',
  standalone: true,
  imports: [CommonModule, NgFor, NgIf], 
  templateUrl: './internship.html',
  styleUrls: ['./internship.css']
})
export class Internship {
  
  // Roles
  roles = [
    {
      title: 'Marketing & Branding',
      items: ['Digital Marketing', 'Social Media Management', 'Content Creation', 'Market Research']
    },
     {
  title: 'TI Development',
  items: [
    'Full-Stack Web Applications','Mobile App Development','API Integration & Backend Solutions','Custom Software Development'
  ]
},
{
  title: 'Web Design',
  items: [
    'Responsive UI/UX Design','Creative Landing Pages','E-commerce Website Design','Brand Identity & Visual Consistency'
  ]
},    {
      title: 'Human Resources (HR)',
      items: ['Recruitment & Onboarding', 'Employee Engagement', 'HR Operations', 'Policy & Compliance']
    },
    {
      title: 'Finance & Accounting',
      items: ['Financial Analysis', 'Budgeting & Forecasting', 'Tax & Compliance Support', 'Audit Preparation']
    },
    {
      title: 'Business Operations',
      items: ['Process Improvement', 'Vendor Coordination', 'Logistics & Supply Chain', 'Project Assistance']
    },
    {
      title: 'Sales & Business Development',
      items: ['Lead Generation', 'Client Communication', 'CRM Management', 'Proposal Drafting']
    },
    {
      title: 'Strategy & Analytics',
      items: ['Data Analysis', 'Business Research', 'Competitor Benchmarking', 'Strategic Planning Support']
    }
  ];

  // Who can apply
  whoCanApply = [
    'Undergraduate & Postgraduate Students',
    'Recent Graduates (0–2 years experience)',
    'Students from business, commerce, management, economics, and related backgrounds',
    'Strong communication, problem-solving, and collaboration skills preferred'
  ];

  // Internship details
  details = [
    'Duration: 4 to 12 weeks (Flexible)',
    'Mode: Online / Hybrid / In-office (based on location and role)',
    'Stipend: May vary based on project & performance',
    'Certificate: Internship Completion Certificate + LOR (for top performers)'
  ];

  // What you’ll gain
  gains = [
    'Hands-on experience in real corporate projects',
    'Guidance from experienced mentors',
    'Exposure to cross-functional teams',
    'Resume-enhancing work experience',
    'Opportunity to build a strong professional network',
    'Improved job-readiness and soft skills'
  ];

  // Slider (optional)
  images = ['internship-hero1.png', 'internship-hero2.png', 'internship-hero3.png'];
  currentIndex = 0;

  constructor() {
    setInterval(() => this.nextSlide(), 4000);
  }

  nextSlide() {
    this.currentIndex = (this.currentIndex + 1) % this.images.length;
  }

  prevSlide() {
    this.currentIndex = (this.currentIndex - 1 + this.images.length) % this.images.length;
  }
}
