import { Component } from '@angular/core';
import { NgFor } from '@angular/common';

@Component({
  selector: 'app-it-courses',
  standalone: true,
  imports: [NgFor],
  templateUrl: './it-courses.html',
  styleUrls: ['./it-courses.css']
})
export class ItCourses {
  courses = [
    {
      title: "💻 Software Development",
      duration: "6–12 Months",
      eligibility: "12th Pass / Graduate",
      highlights: [
        "Programming Languages (Java, Python, C++)",
        "Software Development Life Cycle (SDLC)",
        "Database Management (MySQL, MongoDB)",
        "Version Control (Git), Agile/Scrum"
      ],
      roles: "Software Developer, Backend Engineer, Application Developer",
      image: "software-dev.jpg"
    },
    {
      title: "🧪 Software Testing",
      duration: "3–6 Months",
      eligibility: "12th Pass / Graduate",
      highlights: [
        "Manual Testing & Test Case Writing",
        "Automation Testing (Selenium, TestNG)",
        "Bug Reporting & Tracking (JIRA)",
        "API Testing, Performance Testing (Basics)"
      ],
      roles: "QA Tester, Automation Engineer, Test Analyst",
      image: "software-testing.jpg"
    },
    {
      title: "🌐 Web Development",
      duration: "3–6 Months",
      eligibility: "10th/12th Pass & Above",
      highlights: [
        "HTML, CSS, JavaScript, Bootstrap",
        "React.js / Angular (Frontend)",
        "PHP / Node.js / Python (Backend)",
        "Hosting, Git, Database Integration"
      ],
      roles: "Web Developer, UI Developer, Frontend / Back-end Dev",
      image: "web-dev.jpeg"
    },
    {
      title: "📊 Data Science",
      duration: "6–9 Months",
      eligibility: "Graduate (Preferably in Math/Science/IT)",
      highlights: [
        "Python for Data Science",
        "Data Analysis (NumPy, Pandas)",
        "Machine Learning (Scikit-Learn)",
        "Visualization (Matplotlib, Power BI), SQL"
      ],
      roles: "Data Analyst, Data Scientist, ML Engineer",
      image: "data-science.jpg"
    },
    {
      title: "☁️ Salesforce (SFDC)",
      duration: "3–6 Months",
      eligibility: "12th Pass / Graduate",
      highlights: [
        "Salesforce CRM Fundamentals",
        "Admin & Developer Modules",
        "Apex Programming, Visual-force",
        "Lightning Components, Data Modeling"
      ],
      roles: "Salesforce Admin, SFDC Developer, CRM Consultant",
      image: "salesforce.jpg"
    },
    {
      title: "📘 Programming in C & C++",
      duration: "3 Months",
      fees: "₹5,500",
      code: "PCC033",
      image: "cpp.jpg",
      highlights: [
        "C basics",
        "OOP in C++",
        "Projects"
      ],
      roles: "C Programmer, C++ Developer, Embedded Programmer"
    },
    {
      title: "☕ Certificate in JAVA",
      duration: "3 Months",
      fees: "₹6,500",
      code: "JAV031",
      image: "java.jpeg",
      highlights: [
        "Core Java",
        "OOP concepts",
        "Projects"
      ],
      roles: "Java Developer, Android Developer, Backend Engineer"
    },
    {
      title: "🌍 Certificate in Web Technology",
      duration: "3 Months",
      fees: "₹7,000",
      code: "CWT031",
      image: "web-tech.jpeg",
      highlights: [
        "HTML",
        "CSS",
        "JavaScript basics"
      ],
      roles: "Frontend Developer, Web Designer, Junior Web Developer"
    },
    {
      title: "🎨 Diploma in Web Designing",
      duration: "6 Months",
      fees: "₹12,000",
      code: "DWD061",
      image: "web-design.jpg",
      highlights: [
        "HTML5",
        "CSS3",
        "Responsive Design",
        "Photoshop"
      ],
      roles: "UI/UX Designer, Web Designer, Creative Frontend Developer"
    },
    {
      title: "🐍 Programming in Python & MySQL",
      duration: "6 Months",
      fees: "₹13,000",
      code: "PPM061",
      image: "python-mysql.png",
      highlights: [
        "Python basics",
        "Database",
        "Mini project"
      ],
      roles: "Python Developer, Database Engineer, Backend Developer"
    }
  ];
}
