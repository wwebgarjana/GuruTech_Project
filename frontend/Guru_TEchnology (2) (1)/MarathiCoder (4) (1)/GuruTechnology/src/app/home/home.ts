

import { CommonModule } from '@angular/common';
import { Component, OnInit, OnDestroy, HostListener } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule],
  templateUrl: './home.html',
  styleUrls: ['./home.css']
})
export class Home implements OnInit, OnDestroy {
  
  slides = [
    { src: 'bg1.png', heading: 'Information Technology' },
    { src: 'image2.jpg', heading: 'Basic Computer Classes' },
    { src: 'image3.jpg', heading: 'Internship' },
    { src: 'image4.jpg', heading: 'Career Counselling' }
  ];
  // ✅ Student model for form binding
student = {
  fullName: '',
  email: '',
  phone: '',
  course: '',
  appointment: ''
};

// ✅ Submit form method
async submitForm() {
  try {
    const response = await fetch("http://localhost:8082/api/form/submit", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(this.student)
    });

    const result = await response.text();
    alert(result);

    // Reset form
    this.student = {
      fullName: '',
      email: '',
      phone: '',
      course: '',
      appointment: ''
    };
  } catch (error) {
    alert("❌ Error submitting form: " + error);
  }
}



  // ✅ Course Cards
  cards = [
    {
      image: 'card1.jpg',
      title: 'Information Technology Classes:',
      description:
        'Learn advanced IT concepts, software, and modern technologies to excel in the tech industry.',
      link: '/it-courses'
    },
    {
      image: 'card2.jpg',
      title: 'Basic Computer Classes:',
      description:
        'Understand computer fundamentals, MS Office, internet usage, and essential digital skills.',
      link: '/basic-courses'
    },
    {
      image: 'card3.jpg',
      title: 'Career Counselling:',
      description:
        'Get expert guidance to choose the right career path and unlock your true potential.',
      link: '/career-counselling'
    },
    {
      image: 'card4.jpg',
      title: 'Internship Offers:',
      description:
        'Gain real-world experience through our industry-relevant internship opportunities.',
      link: '/internship'
    }
  ];

  // ✅ Blog Section
  blog = {
    description:
      'Your career is one of the most important journeys of your life, and the right guidance can shape your path toward success. Our career counselling sessions go beyond basic advice — we dive deep into understanding your interests, skills, and aspirations. Whether you’re a student exploring options, a graduate deciding your next step, or a professional looking for a career shift, we provide expert guidance tailored just for you. Through personalized sessions, we help you discover your true potential, explore diverse opportunities, and set clear, achievable goals. Our counsellors not only guide you in choosing the right path but also boost your confidence to overcome challenges along the way. With the right direction, planning, and motivation, you can turn your dreams into reality and step into a future full of possibilities.',
    image: 'blog1.gif'
  };

  // ✅ Alumni Carousel
  alumniList = [
    {
      image: 'alumni1.jpg',
      name: 'Shaumaya Qha',
      role: 'Software Engineer',
      review:
        'Apart from regular live classes, I was assigned with a mentor from Microsoft who guided me very well throughout the course.',
      from: 'J.P.Morgan',
      to: 'Wipro'
    },
    {
      image: 'alumni2.jpg',
      name: 'Subhangi Duhan',
      role: 'Software Researcher',
      review:
        'My focus was on System Design. I learned high-level design concepts that helped me crack Microsoft.',
      from: 'J.P.Morgan',
      to: 'Microsoft',
      hike: '90% Hike'
    },
    {
      image: 'alumni3.jpg',
      name: 'Rahul Verma',
      role: 'Data Scientist',
      review:
        'The program gave me practical exposure and helped me transition into the field of AI/ML.',
      from: 'Infosys',
      to: 'Google'
    },
    {
      image: 'alumni4.jpg',
      name: 'Priya Sharma',
      role: 'Cloud Engineer',
      review:
        'Hands-on projects and mentorship prepared me for AWS Cloud roles.',
      from: 'Cognizant',
      to: 'Amazon'
    },
    {
      image: 'alumni5.jpg',
      name: 'Arjun Mehta',
      role: 'Full Stack Developer',
      review:
        'I got confidence with real projects and landed a developer role.',
      from: 'Capgemini',
      to: 'TCS'
    },
    {
      image: 'alumni6.jpg',
      name: 'Neha Singh',
      role: 'Cybersecurity Analyst',
      review:
        'Expert trainers and live labs made me ready for cybersecurity challenges.',
      from: 'HCL',
      to: 'IBM'
    },
    {
      image: 'alumni7.jpg',
      name: 'Karan Gupta',
      role: 'Business Analyst',
      review:
        'The case studies and practical approach prepared me for business analytics roles.',
      from: 'Accenture',
      to: 'Deloitte'
    }
  ];

  // ✅ Hands-on Projects (fifthpage)
  projects = [
    {
      title: 'Authentication',
      desc: 'Create authentication technology to give access, Correspond to those stored in a database of authorized users.',
      logos: ['microsoft.png', 'google.png'],
      tools: [
        'https://cdn-icons-png.flaticon.com/512/5968/5968322.png', 
        'https://cdn-icons-png.flaticon.com/512/919/919836.png', 
        'https://cdn-icons-png.flaticon.com/512/5968/5968292.png'
      ]
    },
    {
      title: 'E-Commerce Platform',
      desc: 'Develop an online store with product management, cart, and secure payments.',
      logos: ['amazon.png', 'flipkart.png'],
      tools: [
        'https://cdn-icons-png.flaticon.com/512/919/919851.png',
        'https://cdn-icons-png.flaticon.com/512/919/919830.png', 
        'https://cdn-icons-png.flaticon.com/512/919/919825.png'  
      ]
    },
    {
      title: 'Hospital Management',
      desc: 'A complete system for appointments, patient records, and doctor availability.',
      logos: ['apollo.png', 'fortis.png'],
      tools: [
        'https://cdn-icons-png.flaticon.com/512/919/919827.png', 
        'https://cdn-icons-png.flaticon.com/512/919/919836.png', 
        'https://cdn-icons-png.flaticon.com/512/919/919851.png' 
      ]
    }
  ];

  currentProject = 0;

  // ✅ State
  currentSlide = 0;
  currentAlumni = 0;
  interval: any;
  alumniInterval: any;

  ngOnInit(): void {
    this.interval = setInterval(() => this.nextSlide(), 4000);
    this.alumniInterval = setInterval(() => this.nextAlumni(), 3000);

    // Initial scroll check
    this.checkScrollAnimations();
 
  }

  ngOnDestroy(): void {
    clearInterval(this.interval);
    clearInterval(this.alumniInterval);
  }

  // ✅ Hero Slider controls
  nextSlide() {
    this.currentSlide = (this.currentSlide + 1) % this.slides.length;
  }
  prevSlide() {
    this.currentSlide =
      (this.currentSlide - 1 + this.slides.length) % this.slides.length;
  }

  // ✅ Alumni controls
  nextAlumni() {
    this.currentAlumni = (this.currentAlumni + 1) % this.alumniList.length;
  }

  // ✅ Download Brochure
  downloadBrochure() {
    const link = document.createElement('a');
    link.href = 'assets/brochure.pdf';
    link.download = 'Brochure.pdf';
    link.click();
  }

  // ✅ FAQs
  faqs = [
    {
      question: 'How to enroll for a Course?',
      answer:
        'You can enroll by filling out the application form and making the payment online.',
      open: false
    },
    {
      question: 'Can I get the recordings of my previous lectures?',
      answer: 'Yes, all enrolled students get access to recorded sessions anytime.',
      open: false
    },
    {
      question: 'Who would be the instructor for enrolled course?',
      answer:
        'Industry experts and mentors with years of real-world experience will guide you.',
      open: false
    },
    {
      question: 'What kind of placement support will be given post completion of program?',
      answer:
        'We provide resume building, mock interviews, and placement opportunities with top companies.',
      open: false
    }
  ];

  toggleFAQ(index: number) {
    this.faqs[index].open = !this.faqs[index].open;
  }

  // ✅ Fifthpage: Project Navigation
  nextProject() {
    this.currentProject = (this.currentProject + 1) % this.projects.length;
  }
  prevProject() {
    this.currentProject =
      (this.currentProject - 1 + this.projects.length) % this.projects.length;
  }

  // ✅ Scroll Animations
  @HostListener('window:scroll', [])
  onWindowScroll() {
    this.checkScrollAnimations();
  }

  checkScrollAnimations() {
    const sections = document.querySelectorAll(
      '.projects-left, .project-card, .highlight-fee-section'
    );

    sections.forEach((sec: Element) => {
      const rect = sec.getBoundingClientRect();
      if (rect.top < window.innerHeight - 100) {
        sec.classList.add('show');
      }
    });
  }
  
}
