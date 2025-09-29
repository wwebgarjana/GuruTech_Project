import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CareerCounselling } from './career-counselling';

describe('CareerCounselling', () => {
  let component: CareerCounselling;
  let fixture: ComponentFixture<CareerCounselling>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CareerCounselling]
    })
    .compileComponents();

    fixture = TestBed.createComponent(CareerCounselling);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
