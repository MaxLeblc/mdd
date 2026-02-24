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
    // Detects route changes to show/hide the header
    this.router.events
      .pipe(filter((event) => event instanceof NavigationEnd))
      .subscribe((event: any) => {
        const url = event.url;
        // Displays the header on authenticated pages
        this.showHeader.set(
          url.startsWith('/post') ||
            url.startsWith('/posts') ||
            url.startsWith('/post-comment') ||
            url.startsWith('/topics') ||
            url.startsWith('/profile'),
        );
      });
  }
}
