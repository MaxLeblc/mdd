import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, Validators, ReactiveFormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { PostService } from '../../../services/post.service';
import { TopicService } from '../../../services/topic.service';
import { Topic } from '../../../interfaces/topic.interface';
import { MatCardModule } from '@angular/material/card';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatSelectModule } from '@angular/material/select';

@Component({
  selector: 'app-post',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    MatCardModule,
    MatInputModule,
    MatButtonModule,
    MatIconModule,
    MatSelectModule,
  ],
  templateUrl: './post.component.html',
  styleUrl: './post.component.scss',
})
export class PostComponent implements OnInit {
  postForm;
  topics: Topic[] = [];
  errorMessage = '';

  constructor(
    private fb: FormBuilder,
    private postService: PostService,
    private topicService: TopicService,
    private router: Router,
  ) {
    this.postForm = this.fb.group({
      topicId: ['', [Validators.required]],
      title: ['', [Validators.required, Validators.minLength(3)]],
      content: ['', [Validators.required, Validators.minLength(10)]],
    });
  }

  ngOnInit(): void {
    this.topicService.getTopics().subscribe({
      next: (data) => {
        this.topics = data;
      },
      error: () => {
        this.errorMessage = 'Impossible de charger les thèmes';
      },
    });
  }

  onSubmit(): void {
    if (this.postForm.valid) {
      const { topicId, title, content } = this.postForm.value;

      if (!topicId || !title || !content) {
        return;
      }

      this.postService.createPost({ topicId: Number(topicId), title, content }).subscribe({
        next: () => {
          this.router.navigate(['/posts']);
        },
        error: () => {
          this.errorMessage = "Erreur lors de la création de l'article";
        },
      });
    }
  }

  goBack(): void {
    this.router.navigate(['/posts']);
  }
}
