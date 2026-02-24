import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Comment } from '../interfaces/comment.interface';
import { environment } from '../../environments/environment';

@Injectable({
  providedIn: 'root',
})
export class CommentService {
  private apiUrl = `${environment.apiUrl}/posts`;

  constructor(private httpClient: HttpClient) {}

  public getCommentsByPostId(postId: number): Observable<Comment[]> {
    return this.httpClient.get<Comment[]>(`${this.apiUrl}/${postId}/comments`);
  }

  public createComment(postId: number, content: string): Observable<Comment> {
    return this.httpClient.post<Comment>(`${this.apiUrl}/${postId}/comments`, { content });
  }
}
