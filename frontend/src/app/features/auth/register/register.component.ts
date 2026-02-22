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
  selector: 'app-register',
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
  templateUrl: './register.component.html',
  styleUrl: './register.component.scss',
})
export class RegisterComponent {
  hide = true;
  errorMessage = '';
  registerForm;

  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private router: Router,
  ) {
    this.registerForm = this.fb.group({
      username: ['', [Validators.required, Validators.minLength(3)]],
      email: ['', [Validators.required, Validators.email]],
      password: [
        '',
        [
          Validators.required,
          Validators.minLength(8),
          Validators.pattern(
            /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]{8,}$/,
          ),
        ],
      ],
    });
  }

  onSubmit(): void {
    if (this.registerForm.valid) {
      const { username, email, password } = this.registerForm.value;

      if (!username || !email || !password) {
        return;
      }

      this.authService.register({ username, email, password }).subscribe({
        next: (response) => {
          this.authService.saveToken(response.token);
          this.router.navigate(['/posts']);
        },
        error: (err) => {
          if (err.status === 400 && err.error?.message) {
            if (err.error.message.includes('Username')) {
              this.errorMessage = "Ce nom d'utilisateur est déjà utilisé.";
            } else if (err.error.message.includes('Email')) {
              this.errorMessage = 'Cet email est déjà utilisé.';
            } else {
              this.errorMessage = err.error.message;
            }
          } else if (err.status === 401) {
            this.errorMessage =
              'Le mot de passe doit contenir au moins 8 caractères avec 1 majuscule, 1 minuscule, 1 chiffre et 1 caractère spécial (@$!%*?&).';
          } else {
            this.errorMessage = "Erreur lors de l'inscription. Veuillez vérifier vos informations.";
          }
        },
      });
    }
  }
}
