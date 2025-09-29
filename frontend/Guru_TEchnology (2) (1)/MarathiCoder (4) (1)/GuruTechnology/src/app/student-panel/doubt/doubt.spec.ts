import { ComponentFixture, TestBed } from '@angular/core/testing';

import { Doubt } from './doubt';

describe('Doubt', () => {
  let component: Doubt;
  let fixture: ComponentFixture<Doubt>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [Doubt]
    })
    .compileComponents();

    fixture = TestBed.createComponent(Doubt);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
