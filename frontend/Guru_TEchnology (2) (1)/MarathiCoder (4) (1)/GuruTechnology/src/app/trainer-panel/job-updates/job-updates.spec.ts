import { ComponentFixture, TestBed } from '@angular/core/testing';

import { JobUpdates } from './job-updates';

describe('JobUpdates', () => {
  let component: JobUpdates;
  let fixture: ComponentFixture<JobUpdates>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [JobUpdates]
    })
    .compileComponents();

    fixture = TestBed.createComponent(JobUpdates);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
