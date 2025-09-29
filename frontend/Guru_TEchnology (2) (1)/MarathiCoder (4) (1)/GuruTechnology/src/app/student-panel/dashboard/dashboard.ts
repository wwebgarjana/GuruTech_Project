





import { Component, OnInit } from '@angular/core';
import { Router, RouterModule } from '@angular/router';
import { NotificationService, Notification } from '../../services/notification.service';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [RouterModule, CommonModule],
  templateUrl: './dashboard.html',
  styleUrls: ['./dashboard.css']
})
export class Dashboard implements OnInit {
   userEmail: string | null = null;
  notifications: Notification[] = [];
  unreadCount = 0;
  showNotifications = false;

  constructor(private router: Router, private notificationService: NotificationService) {}

  ngOnInit(): void {
    this.userEmail = localStorage.getItem('userEmail');
    if (this.userEmail) this.loadNotifications();
    else console.warn('Student email not found!');

    setInterval(() => this.loadNotifications(), 10000);
  }

  loadNotifications(): void {
    if (!this.userEmail) return;

    this.notificationService.getStudentNotifications(this.userEmail).subscribe({
      next: (data) => {
        this.notifications = data.sort((a, b) =>
          new Date(b.createdAt).getTime() - new Date(a.createdAt).getTime()
        );
        this.unreadCount = this.notifications.filter(n => !n.readStatus).length;
      },
      error: (err) => console.error(err)
    });
  }

  toggleNotifications(): void {
    this.showNotifications = !this.showNotifications;
  }

  markAsRead(id: number): void {
  // Remove notification from the list immediately
  this.notifications = this.notifications.filter(n => n.id !== id);

  // Update unread count
  this.unreadCount = this.notifications.filter(n => !n.readStatus).length;

  // Optionally, mark as read on server (non-blocking)
  this.notificationService.markAsRead(id).subscribe({
    next: () => {
      console.log('Notification marked as read on server');
    },
    error: (err: any) => console.error('Failed to mark notification as read:', err)
  });
}

  logout(): void {
    localStorage.clear();
    this.router.navigate(['/home']);
  }

  goToProfile(): void {
    this.router.navigate(['/student-panel/dashboard/profile']);
  }
}