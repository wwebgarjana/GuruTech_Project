import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CourseManage } from './course-manage';

describe('CourseManage', () => {
  let component: CourseManage;
  let fixture: ComponentFixture<CourseManage>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CourseManage]
    })
    .compileComponents();

    fixture = TestBed.createComponent(CourseManage);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
