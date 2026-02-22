import { Component, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { TopicService } from '../../../services/topic.service';
import { UserService } from '../../../services/user.service';
import { AuthService } from '../../../services/auth.service';
import { Topic } from '../../../interfaces/topic.interface';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';

@Component({
  selector: 'app-topic-list',
  standalone: true,
  imports: [CommonModule, MatCardModule, MatButtonModule],
  templateUrl: './topic-list.component.html',
  styleUrl: './topic-list.component.scss',
})
export class TopicListComponent implements OnInit {
  topics = signal<Topic[]>([]);
  subscribedTopicIds = signal<Set<number>>(new Set());
  errorMessage = signal('');
  userId: number | null = null;

  constructor(
    private topicService: TopicService,
    private userService: UserService,
    private authService: AuthService,
  ) {}

  ngOnInit(): void {
    this.userId = this.authService.getUserId();

    if (!this.userId) {
      this.errorMessage.set('Utilisateur non connecté');
      return;
    }

    // Charger les topics
    this.topicService.getTopics().subscribe({
      next: (topics) => {
        this.topics.set(topics);
      },
      error: () => {
        this.errorMessage.set('Impossible de charger les thèmes');
      },
    });

    // Charger les abonnements de l'utilisateur
    this.userService.getUserById(this.userId).subscribe({
      next: (user) => {
        const subscribedIds = new Set(user.subscriptions.map((topic) => topic.id));
        this.subscribedTopicIds.set(subscribedIds);
      },
      error: () => {
        this.errorMessage.set('Impossible de charger vos abonnements');
      },
    });
  }

  isSubscribed(topicId: number): boolean {
    return this.subscribedTopicIds().has(topicId);
  }

  toggleSubscription(topicId: number): void {
    if (!this.userId) return;

    const isCurrentlySubscribed = this.isSubscribed(topicId);

    if (isCurrentlySubscribed) {
      // Se désabonner
      this.userService.unsubscribe(this.userId, topicId).subscribe({
        next: () => {
          const newSet = new Set(this.subscribedTopicIds());
          newSet.delete(topicId);
          this.subscribedTopicIds.set(newSet);
        },
        error: () => {
          this.errorMessage.set('Erreur lors du désabonnement');
        },
      });
    } else {
      // S'abonner
      this.userService.subscribe(this.userId, topicId).subscribe({
        next: () => {
          const newSet = new Set(this.subscribedTopicIds());
          newSet.add(topicId);
          this.subscribedTopicIds.set(newSet);
        },
        error: () => {
          this.errorMessage.set("Erreur lors de l'abonnement");
        },
      });
    }
  }
}
