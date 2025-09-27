import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RevokeStudent } from './revoke-student';

describe('RevokeStudent', () => {
  let component: RevokeStudent;
  let fixture: ComponentFixture<RevokeStudent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [RevokeStudent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(RevokeStudent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
