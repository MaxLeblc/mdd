import { Component, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterModule } from '@angular/router';
import { PostService } from '../../../services/post.service';
import { Post } from '../../../interfaces/post.interface';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';

@Component({
  selector: 'app-post-list',
  standalone: true,
  imports: [CommonModule, RouterModule, MatCardModule, MatButtonModule, MatIconModule],
  templateUrl: './post-list.component.html',
  styleUrls: ['./post-list.component.scss'],
})
export class PostListComponent implements OnInit {
  posts = signal<Post[]>([]);
  errorMessage = signal('');
  sortAscending = signal(false); // false = plus récent en premier (desc), true = plus ancien en premier (asc)

  constructor(
    private postService: PostService,
    private router: Router,
  ) {}

  ngOnInit(): void {
    this.postService.getFeed().subscribe({
      next: (data) => {
        this.posts.set(data);
        this.sortPosts();
      },
      error: () => {
        this.errorMessage.set('Impossible de charger les articles');
      },
    });
  }

  toggleSort(): void {
    this.sortAscending.set(!this.sortAscending());
    this.sortPosts();
  }

  private sortPosts(): void {
    const sorted = [...this.posts()].sort((a, b) => {
      const dateA = new Date(a.createdAt).getTime();
      const dateB = new Date(b.createdAt).getTime();
      return this.sortAscending() ? dateA - dateB : dateB - dateA;
    });
    this.posts.set(sorted);
  }

  navigateToPost(postId: number): void {
    this.router.navigate(['/posts', postId]);
  }
}
