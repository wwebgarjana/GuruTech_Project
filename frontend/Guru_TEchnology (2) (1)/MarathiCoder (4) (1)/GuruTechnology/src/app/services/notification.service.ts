import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface Notification {
  id: number;
  senderRole: string;
  receiverRole: string;
  batch: string;
  receiverEmail?: string;
  message: string;
  readStatus: boolean;
  createdAt: string;
}

@Injectable({
  providedIn: 'root'
})
export class NotificationService {
  private apiUrl = 'http://localhost:8082/api/notifications';

  constructor(private http: HttpClient) {}

  getStudentNotifications(email: string): Observable<Notification[]> {
    return this.http.get<Notification[]>(`${this.apiUrl}/student/${encodeURIComponent(email)}`);
  }

getTrainerNotifications(email: string): Observable<Notification[]> {
    return this.http.get<Notification[]>(`${this.apiUrl}/trainer/${encodeURIComponent(email)}`);
  }

  getAdminNotifications(email: string): Observable<Notification[]> {
    return this.http.get<Notification[]>(`${this.apiUrl}/admin/${encodeURIComponent(email)}`);
  }

  markAsRead(notificationId: number): Observable<any> {
    return this.http.post(`${this.apiUrl}/mark-read/${notificationId}`, {});
  }
}
