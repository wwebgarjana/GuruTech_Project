import { ComponentFixture, TestBed } from '@angular/core/testing';

import { BasicCourses } from './basic-courses';

describe('BasicCourses', () => {
  let component: BasicCourses;
  let fixture: ComponentFixture<BasicCourses>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [BasicCourses]
    })
    .compileComponents();

    fixture = TestBed.createComponent(BasicCourses);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
