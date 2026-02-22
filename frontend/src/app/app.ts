import { Component, signal } from '@angular/core';
import { RouterOutlet, Router, NavigationEnd } from '@angular/router';
import { CommonModule } from '@angular/common';
import { HeaderComponent } from './features/header/header.component';
import { filter } from 'rxjs';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [CommonModule, RouterOutlet, HeaderComponent],
  templateUrl: './app.html',
  styleUrl: './app.scss',
})
export class App {
  protected readonly title = signal('frontend');
  showHeader = signal(false);

  constructor(private router: Router) {
    // Détecte les changements de route pour afficher/masquer le header
    this.router.events
      .pipe(filter((event) => event instanceof NavigationEnd))
      .subscribe((event: any) => {
        const url = event.url;
        // Affiche le header sur les pages authentifiées
        this.showHeader.set(
          url.startsWith('/posts') || url.startsWith('/topics') || url.startsWith('/profile'),
        );
      });
  }
}
