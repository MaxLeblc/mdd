import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { User } from '../interfaces/user.interface';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root',
})
export class UserService {
  private apiUrl = `${environment.apiUrl}/users`;

  constructor(private http: HttpClient) {}

  getUserById(id: number): Observable<User> {
    return this.http.get<User>(`${this.apiUrl}/${id}`);
  }

  subscribe(userId: number, topicId: number): Observable<void> {
    return this.http.post<void>(`${this.apiUrl}/${userId}/subscribe/${topicId}`, {});
  }

  unsubscribe(userId: number, topicId: number): Observable<void> {
    return this.http.post<void>(`${this.apiUrl}/${userId}/unsubscribe/${topicId}`, {});
  }
}
