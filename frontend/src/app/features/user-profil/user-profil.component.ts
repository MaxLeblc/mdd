import { Component, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatInputModule } from '@angular/material/input';
import { MatFormFieldModule } from '@angular/material/form-field';
import { UserService } from '../../services/user.service';
import { AuthService } from '../../services/auth.service';
import { User } from '../../interfaces/user.interface';
import { Topic } from '../../interfaces/topic.interface';
import { MatIconModule } from '@angular/material/icon';

@Component({
  selector: 'app-user-profil',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    MatCardModule,
    MatButtonModule,
    MatInputModule,
    MatIconModule,
    MatFormFieldModule,
  ],
  templateUrl: './user-profil.component.html',
  styleUrl: './user-profil.component.scss',
})
export class UserProfilComponent implements OnInit {
  user = signal<User | null>(null);
  subscriptions = signal<Topic[]>([]);
  errorMessage = signal('');
  successMessage = signal('');
  profileForm;

  constructor(
    private fb: FormBuilder,
    private userService: UserService,
    private authService: AuthService,
    private router: Router,
  ) {
    this.profileForm = this.fb.group({
      username: ['', [Validators.required, Validators.minLength(3)]],
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.minLength(8)]],
    });
  }

  ngOnInit(): void {
    this.loadUserData();
  }

  loadUserData(): void {
    const userId = this.authService.getUserId();
    if (userId) {
      this.userService.getUserById(userId).subscribe({
        next: (data) => {
          this.user.set(data);
          this.subscriptions.set(data.subscriptions || []);
          this.profileForm.patchValue({
            username: data.username,
            email: data.email,
          });
        },
        error: () => {
          this.errorMessage.set('Impossible de charger les données utilisateur');
        },
      });
    }
  }

  onSubmit(): void {
    if (this.profileForm.valid && this.user()) {
      const formValue = this.profileForm.value;
      const updateData: any = {
        username: formValue.username,
        email: formValue.email,
      };

      // Add the password only if it has been changed
      if (formValue.password && formValue.password.trim()) {
        updateData.password = formValue.password;
      }

      this.userService.updateUser(this.user()!.id, updateData).subscribe({
        next: (updatedUser) => {
          this.successMessage.set('Profil mis à jour avec succès');
          this.errorMessage.set('');

          this.user.set(updatedUser);
          this.profileForm.patchValue({
            username: updatedUser.username,
            email: updatedUser.email,
            password: '',
          });
        },
        error: () => {
          this.errorMessage.set('Erreur lors de la mise à jour du profil');
          this.successMessage.set('');
        },
      });
    }
  }

  unsubscribeFromTopic(topicId: number): void {
    const userId = this.authService.getUserId();
    if (userId) {
      this.userService.unsubscribe(userId, topicId).subscribe({
        next: () => {
          this.subscriptions.update((current) => current.filter((t) => t.id !== topicId));
          this.successMessage.set('Désabonnement réussi');
          this.errorMessage.set('');
        },
        error: () => {
          this.errorMessage.set('Erreur lors du désabonnement');
          this.successMessage.set('');
        },
      });
    }
  }

  logout(): void {
    this.authService.logout();
    this.router.navigate(['/login']);
  }
}
