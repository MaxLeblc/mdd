import { Component, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, Validators, ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { PostService } from '../../../services/post.service';
import { CommentService } from '../../../services/comment.service';
import { Post } from '../../../interfaces/post.interface';
import { Comment } from '../../../interfaces/comment.interface';
import { MatCardModule } from '@angular/material/card';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';

@Component({
  selector: 'app-post-comment',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    MatCardModule,
    MatInputModule,
    MatButtonModule,
    MatIconModule,
  ],
  templateUrl: './post-comment.component.html',
  styleUrl: './post-comment.component.scss',
})
export class PostCommentComponent implements OnInit {
  post = signal<Post | null>(null);
  comments = signal<Comment[]>([]);
  commentForm;
  errorMessage = signal('');
  loading = signal(true);

  constructor(
    private fb: FormBuilder,
    private postService: PostService,
    private commentService: CommentService,
    private route: ActivatedRoute,
    private router: Router,
  ) {
    this.commentForm = this.fb.group({
      content: ['', [Validators.minLength(3)]],
    });
  }

  ngOnInit(): void {
    const postId = Number(this.route.snapshot.paramMap.get('id'));
    if (postId) {
      this.loadPost(postId);
      this.loadComments(postId);
    }
  }

  loadPost(postId: number): void {
    this.postService.getPostById(postId).subscribe({
      next: (data) => {
        this.post.set(data);
        this.loading.set(false);
      },
      error: (err) => {
        console.error("Erreur lors du chargement de l'article:", err);
        this.errorMessage.set("Impossible de charger l'article");
        this.loading.set(false);
      },
    });
  }

  loadComments(postId: number): void {
    this.commentService.getCommentsByPostId(postId).subscribe({
      next: (data) => {
        this.comments.set(data);
        console.log('Commentaires chargés:', data);
      },
      error: (err) => {
        console.error('Erreur lors du chargement des commentaires:', err);
      },
    });
  }

  onSubmitComment(): void {
    if (this.commentForm.valid && this.post() && this.commentForm.value.content?.trim()) {
      const { content } = this.commentForm.value;

      if (!content || !content.trim()) {
        return;
      }

      this.commentService.createComment(this.post()!.id, content).subscribe({
        next: () => {
          this.loadComments(this.post()!.id);
          this.commentForm.reset();
        },
        error: () => {
          this.errorMessage.set('Erreur lors de la création du commentaire');
        },
      });
    }
  }

  goBack(): void {
    this.router.navigate(['/posts']);
  }
}
