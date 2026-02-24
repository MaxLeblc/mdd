import { Component, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, Validators, ReactiveFormsModule } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { PostService } from '../../../services/post.service';
import { CommentService } from '../../../services/comment.service';
import { AuthService } from '../../../services/auth.service';
import { UserService } from '../../../services/user.service';
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
  currentUsername = signal<string>('Utilisateur');

  constructor(
    private fb: FormBuilder,
    private postService: PostService,
    private commentService: CommentService,
    private authService: AuthService,
    private userService: UserService,
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
    this.loadCurrentUser();
  }

  loadCurrentUser(): void {
    const userId = this.authService.getUserId();
    if (userId) {
      this.userService.getUserById(userId).subscribe({
        next: (user) => {
          this.currentUsername.set(user.username);
        },
        error: () => {
          console.error("Impossible de charger l'utilisateur");
        },
      });
    }
  }

  loadPost(postId: number): void {
    this.postService.getPostById(postId).subscribe({
      next: (data) => {
        this.post.set(data);
        this.loading.set(false);
        console.log('Article chargé:', data);
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
        // On ne bloque pas l'affichage si les commentaires échouent
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
        next: (newComment) => {
          console.log('Nouveau commentaire créé:', newComment);
          // Recharger les commentaires pour avoir les infos complètes du backend
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
