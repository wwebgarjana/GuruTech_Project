import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ItCourses } from './it-courses';

describe('ItCourses', () => {
  let component: ItCourses;
  let fixture: ComponentFixture<ItCourses>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ItCourses]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ItCourses);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
