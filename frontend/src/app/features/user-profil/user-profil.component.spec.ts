import { ComponentFixture, TestBed } from '@angular/core/testing';

import { UserProfilComponent } from './user-profil.component';

describe('UserProfilComponent', () => {
  let component: UserProfilComponent;
  let fixture: ComponentFixture<UserProfilComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [UserProfilComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(UserProfilComponent);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
