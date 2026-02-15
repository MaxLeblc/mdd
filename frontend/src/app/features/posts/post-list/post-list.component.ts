import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common'; // Important pour *ngFor et DatePipe
import { PostService } from '../../../services/post.service';
import { Post } from '../../../interfaces/post.interface';
import { MatCardModule } from '@angular/material/card'; // Pour les jolies cartes
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';

@Component({
  selector: 'app-post-list',
  standalone: true,
  imports: [CommonModule, MatCardModule, MatButtonModule, MatIconModule],
  templateUrl: './post-list.component.html',
  styleUrls: ['./post-list.component.scss'],
})
export class PostListComponent implements OnInit {
  posts: Post[] = [];
  errorMessage = '';

  constructor(private postService: PostService) {}

  ngOnInit(): void {
    this.postService.getPosts().subscribe({
      next: (data) => {
        this.posts = data;
      },
      error: () => {
        this.errorMessage = 'Impossible de charger les articles';
      },
    });
  }
}
