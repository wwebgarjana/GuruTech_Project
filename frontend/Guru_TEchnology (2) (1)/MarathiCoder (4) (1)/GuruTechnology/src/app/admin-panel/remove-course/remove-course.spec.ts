import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RemoveCourse } from './remove-course';

describe('RemoveCourse', () => {
  let component: RemoveCourse;
  let fixture: ComponentFixture<RemoveCourse>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [RemoveCourse]
    })
    .compileComponents();

    fixture = TestBed.createComponent(RemoveCourse);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
