import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RevokedUsers } from './revoked-users';

describe('RevokedUsers', () => {
  let component: RevokedUsers;
  let fixture: ComponentFixture<RevokedUsers>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [RevokedUsers]
    })
    .compileComponents();

    fixture = TestBed.createComponent(RevokedUsers);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
