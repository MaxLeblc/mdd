import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, Validators, ReactiveFormsModule } from '@angular/forms';
import { Router, RouterModule } from '@angular/router';
import { AuthService } from '../../../services/auth.service';
import { MatCardModule } from '@angular/material/card';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    RouterModule,
    MatCardModule,
    MatInputModule,
    MatButtonModule,
    MatIconModule,
  ],
  templateUrl: './login.component.html',
  styleUrl: './login.component.scss',
})
export class LoginComponent {
  hide = true;
  errorMessage = '';
  loginForm;

  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private router: Router,
  ) {
    // Initialization of the FormBuilder in the constructor
    this.loginForm = this.fb.group({
      emailOrUsername: ['', [Validators.required]],
      password: ['', [Validators.required]],
    });
  }

  onSubmit(): void {
    if (this.loginForm.valid) {
      const { emailOrUsername, password } = this.loginForm.value;

      if (!emailOrUsername || !password) {
        return;
      }

      this.authService.login({ emailOrUsername, password }).subscribe({
        next: (response) => {
          this.authService.saveToken(response.token);
          this.authService.saveUserId(response.id);
          this.router.navigate(['/posts']);
        },
        error: (err) => {
          if (err.status === 401) {
            this.errorMessage = "Email/nom d'utilisateur ou mot de passe incorrect";
          } else if (err.status === 403) {
            this.errorMessage = 'Accès refusé';
          } else {
            this.errorMessage = 'Erreur de connexion. Veuillez réessayer.';
          }
        },
      });
    }
  }
}
