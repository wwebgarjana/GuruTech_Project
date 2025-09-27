import { ComponentFixture, TestBed } from '@angular/core/testing';

import { StudentProgressTracker } from './student-progress-tracker';

describe('StudentProgressTracker', () => {
  let component: StudentProgressTracker;
  let fixture: ComponentFixture<StudentProgressTracker>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [StudentProgressTracker]
    })
    .compileComponents();

    fixture = TestBed.createComponent(StudentProgressTracker);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
