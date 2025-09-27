import { ComponentFixture, TestBed } from '@angular/core/testing';

import { StudSubmitAssign } from './stud-submit-assign';

describe('StudSubmitAssign', () => {
  let component: StudSubmitAssign;
  let fixture: ComponentFixture<StudSubmitAssign>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [StudSubmitAssign]
    })
    .compileComponents();

    fixture = TestBed.createComponent(StudSubmitAssign);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
