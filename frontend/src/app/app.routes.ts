import { Routes } from '@angular/router';
import { HomeComponent } from './features/home/home.component';
import { LoginComponent } from './features/auth/login/login.component';
import { RegisterComponent } from './features/auth/register/register.component';
import { PostListComponent } from './features/posts/post-list/post-list.component';
import { PostComponent } from './features/posts/post/post.component';
import { PostCommentComponent } from './features/posts/post-comment/post-comment.component';
import { TopicListComponent } from './features/topics/topic-list/topic-list.component';
import { UserProfilComponent } from './features/user-profil/user-profil.component';

export const routes: Routes = [
  { path: '', component: HomeComponent },
  { path: 'login', component: LoginComponent },
  { path: 'register', component: RegisterComponent },
  { path: 'posts', component: PostListComponent },
  { path: 'posts/:id', component: PostCommentComponent },
  { path: 'post', component: PostComponent },
  { path: 'topics', component: TopicListComponent },
  { path: 'profile', component: UserProfilComponent },
];
