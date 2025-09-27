// import { Component } from '@angular/core';
// import { Router, NavigationEnd, RouterOutlet } from '@angular/router';
// import { Navbar } from './navbar/navbar';
// import { Footer } from './footer/footer';
// import { Home } from './home/home';
// import { About } from './about/about';
// import { Courses } from './courses/courses';
// import { Events } from './events/events';
// import { Blog } from './blog/blog';
// import { Login } from './login/login';
// import { Loader } from './loader/loader';

// import { NgIf } from '@angular/common';

// @Component({
//   selector: 'app-root',
//   standalone: true,
//   imports: [
//     RouterOutlet,
//     NgIf,
//     Navbar,
//     Footer,
//     Home,
//     About,
//     Courses,
//     Events,
//     Blog,
//     Login,
//     Loader
   
//   ],
//   templateUrl: './app.html',
//   styleUrls: ['./app.css']
// })
// export class App {
//   showContent = false;

//   constructor(private router: Router) {
//     this.router.events.subscribe(event => {
//       if (event instanceof NavigationEnd) {
//         // no need for separate checkLoginStatus anymore
//       }
//     });
//   }

//   ngOnInit() {
//     setTimeout(() => {
//       this.showContent = true;
//     }, 2000);
//   }

//   get isLoggedIn(): boolean {
//     return !!localStorage.getItem('role');
//   }
// }



// import { Component } from '@angular/core';
// import { Router, NavigationEnd, RouterOutlet } from '@angular/router';
// import { Navbar } from './navbar/navbar';
// import { Footer } from './footer/footer';
// import { Home } from './home/home';
// import { About } from './about/about';
// import { Courses } from './courses/courses';
// import { Events } from './events/events';
// import { Blog } from './blog/blog';
// import { Login } from './login/login';
// import { Loader } from './loader/loader';
// import { NgIf, NgClass } from '@angular/common';

// @Component({
//   selector: 'app-root',
//   standalone: true,
//   imports: [
//     RouterOutlet,
//     NgIf,
//     NgClass,
//     Navbar,
//     Footer,
//     Home,
//     About,
//     Courses,
//     Events,
//     Blog,
//     Login,
//     Loader
//   ],
//   templateUrl: './app.html',
//   styleUrls: ['./app.css']
// })
// export class App {
//   showContent = false;
//   currentRoute: string = '';

//   constructor(private router: Router) {
//     this.router.events.subscribe(event => {
//       if (event instanceof NavigationEnd) {
//         this.currentRoute = event.urlAfterRedirects;

//         // 🔹 Disable scroll on body for specific dashboard routes
//         const dashboardRoutes = [
//           '/trainer-panel/dashboard',
//           '/student-panel/dashboard',
//           '/admin-panel/dashboard'
//         ];

//         if (dashboardRoutes.some(path => this.currentRoute.startsWith(path))) {
//           document.body.classList.add('disable-scroll');
//         } else {
//           document.body.classList.remove('disable-scroll');
//         }
//       }
//     });
//   }

//   ngOnInit() {
//     setTimeout(() => {
//       this.showContent = true;
//     }, 2000);
//   }

//   get isLoggedIn(): boolean {
//     return !!localStorage.getItem('role');
//   }

//   shouldShowNavbar(): boolean {
//     const allowedRoutes = [
//       '/',
//       '/home',
//       '/about',
//       '/courses',
//       '/events',
//       '/blog',
//       '/login'
//     ];
//     return allowedRoutes.includes(this.currentRoute);
//   }

//   shouldShowFooter(): boolean {
//     const hiddenRoutes = [
//       '/admin-panel/dashboard',
//       '/student-panel/dashboard',
//       '/trainer-panel/dashboard'
//     ];
//     return !hiddenRoutes.includes(this.currentRoute);
//   }
// }




// import { Component } from '@angular/core';
// import { Router, NavigationEnd, RouterOutlet } from '@angular/router';
// import { Navbar } from './navbar/navbar';
// import { Footer } from './footer/footer';
// import { Home } from './home/home';
// import { About } from './about/about';
// import { Courses } from './courses/courses';
// import { Events } from './events/events';
// import { Blog } from './blog/blog';
// import { Login } from './login/login';
// import { Loader } from './loader/loader';

// import { NgIf } from '@angular/common';

// @Component({
//   selector: 'app-root',
//   standalone: true,
//   imports: [
//     RouterOutlet,
//     NgIf,
//     Navbar,
//     Footer,
//     Home,
//     About,
//     Courses,
//     Events,
//     Blog,
//     Login,
//     Loader
   
//   ],
//   templateUrl: './app.html',
//   styleUrls: ['./app.css']
// })
// export class App {
//   showContent = false;

//   constructor(private router: Router) {
//     this.router.events.subscribe(event => {
//       if (event instanceof NavigationEnd) {
//         // no need for separate checkLoginStatus anymore
//       }
//     });
//   }

//   ngOnInit() {
//     setTimeout(() => {
//       this.showContent = true;
//     }, 2000);
//   }

//   get isLoggedIn(): boolean {
//     return !!localStorage.getItem('role');
//   }
// }



// import { Component } from '@angular/core';
// import { Router, NavigationEnd, RouterOutlet } from '@angular/router';
// import { Navbar } from './navbar/navbar';
// import { Footer } from './footer/footer';
// import { Home } from './home/home';
// import { About } from './about/about';
// import { Courses } from './courses/courses';
// import { Events } from './events/events';
// import { Blog } from './blog/blog';
// import { Login } from './login/login';
// import { Loader } from './loader/loader';
// import { NgIf, NgClass } from '@angular/common';

// @Component({
//   selector: 'app-root',
//   standalone: true,
//   imports: [
//     RouterOutlet,
//     NgIf,
//     NgClass,
//     Navbar,
//     Footer,
//     Home,
//     About,
//     Courses,
//     Events,
//     Blog,
//     Login,
//     Loader
//   ],
//   templateUrl: './app.html',
//   styleUrls: ['./app.css']
// })
// export class App {
//   showContent = false;
//   currentRoute: string = '';

//   constructor(private router: Router) {
//     this.router.events.subscribe(event => {
//       if (event instanceof NavigationEnd) {
//         this.currentRoute = event.urlAfterRedirects;

//         // 🔹 Disable scroll on body for specific dashboard routes
//         const dashboardRoutes = [
//           '/trainer-panel/dashboard',
//           '/student-panel/dashboard',
//           '/admin-panel/dashboard'
//         ];

//         if (dashboardRoutes.some(path => this.currentRoute.startsWith(path))) {
//           document.body.classList.add('disable-scroll');
//         } else {
//           document.body.classList.remove('disable-scroll');
//         }
//       }
//     });
//   }

//   ngOnInit() {
//     setTimeout(() => {
//       this.showContent = true;
//     }, 2000);
//   }

//   get isLoggedIn(): boolean {
//     return !!localStorage.getItem('role');
//   }

//   shouldShowNavbar(): boolean {
//     const allowedRoutes = [
//       '/',
//       '/home',
//       '/about',
//       '/courses',
//       '/events',
//       '/blog',
//       '/login'
//     ];
//     return allowedRoutes.includes(this.currentRoute);
//   }

//   shouldShowFooter(): boolean {
//     const hiddenRoutes = [
//       '/admin-panel/dashboard',
//       '/student-panel/dashboard',
//       '/trainer-panel/dashboard'
//     ];
//     return !hiddenRoutes.includes(this.currentRoute);
//   }
// }
import { Component } from '@angular/core';
import { Router, NavigationEnd, RouterOutlet } from '@angular/router';
import { Navbar } from './navbar/navbar';
import { Footer } from './footer/footer';
import { Home } from './home/home';
import { About } from './about/about';
import { Courses } from './courses/courses';
import { Events } from './events/events';
import { Blog } from './blog/blog';
import { Login } from './login/login';
import { Loader } from './loader/loader';
import { NgIf, NgClass } from '@angular/common';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [
    RouterOutlet,
    NgIf,
    NgClass,
    Navbar,
    Footer,
    Home,
    About,
    Courses,
    Events,
    Blog,
    Login,
    Loader
  ],
  templateUrl: './app.html',
  styleUrls: ['./app.css']
})
export class App {
  showContent = false;
  currentRoute: string = '';

  constructor(private router: Router) {
    this.router.events.subscribe(event => {
      if (event instanceof NavigationEnd) {
        this.currentRoute = event.urlAfterRedirects;

        // 🔹 Disable scroll on dashboards
        const dashboardRoutes = [
          '/trainer-panel/dashboard',
          '/student-panel/dashboard',
          '/admin-panel/dashboard'
        ];

        if (dashboardRoutes.some(path => this.currentRoute.startsWith(path))) {
          document.body.classList.add('disable-scroll');
        } else {
          document.body.classList.remove('disable-scroll');
        }
      }
    });
  }

  ngOnInit() {
    setTimeout(() => {
      this.showContent = true;
    }, 2000);
  }

  get isLoggedIn(): boolean {
    return !!localStorage.getItem('role');
  }

  shouldShowNavbar(): boolean {
    const allowedRoutes = [
      '/',
      '/home',
      '/about',
      '/courses',
      '/basic-courses',
      '/it-courses',
      '/internship',
      '/career-counselling',
      '/events',
      '/blog',
      '/login'
    ];
    return allowedRoutes.includes(this.currentRoute);
  }

  shouldShowFooter(): boolean {
    const hiddenRoutes = [
      '/admin-panel/dashboard',
      '/student-panel/dashboard',
      '/trainer-panel/dashboard'
    ];
    return !hiddenRoutes.includes(this.currentRoute);
  }
}
