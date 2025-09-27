import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EnrollCourses } from './enroll-courses';

describe('EnrollCourses', () => {
  let component: EnrollCourses;
  let fixture: ComponentFixture<EnrollCourses>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [EnrollCourses]
    })
    .compileComponents();

    fixture = TestBed.createComponent(EnrollCourses);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
