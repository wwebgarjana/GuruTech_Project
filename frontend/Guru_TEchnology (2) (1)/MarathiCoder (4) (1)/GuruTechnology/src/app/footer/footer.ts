import { Component } from '@angular/core';
import { Router, RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-footer',
  standalone: true,
  imports: [RouterModule,CommonModule ],
  templateUrl: './footer.html',
  styleUrls: ['./footer.css']
})
export class Footer {
  constructor(public router: Router) {}
shouldShowFooter(): boolean {
  const hiddenRoutes = [
    '/admin-panel/dashboard',
    '/trainer-panel/dashboard',
    '/student-panel/dashboard'
  ];

  return !hiddenRoutes.includes(this.router.url);
}

}