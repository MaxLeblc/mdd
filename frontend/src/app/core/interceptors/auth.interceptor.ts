import { HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { Router } from '@angular/router';
import { catchError, throwError } from 'rxjs';
import { AuthService } from '../../services/auth.service';

export const authInterceptor: HttpInterceptorFn = (req, next) => {
  const authService = inject(AuthService);
  const router = inject(Router);
  const token = authService.getToken();

  // Ne pas ajouter le token pour les endpoints d'authentification
  const isAuthEndpoint = req.url.includes('/api/auth/');

  // If we have a token and it's not an auth endpoint, we add it to the Authorization header.
  if (token && !isAuthEndpoint) {
    const clonedRequest = req.clone({
      setHeaders: {
        Authorization: `Bearer ${token}`,
      },
    });
    return next(clonedRequest).pipe(
      catchError((error) => {
        // Si le token est expiré ou invalide (401), déconnecter l'utilisateur
        if (error.status === 401) {
          authService.logout();
          router.navigate(['/login']);
        }
        return throwError(() => error);
      }),
    );
  }

  return next(req);
};
