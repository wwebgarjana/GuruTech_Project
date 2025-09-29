import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RevokeTrainer } from './revoke-trainer';

describe('RevokeTrainer', () => {
  let component: RevokeTrainer;
  let fixture: ComponentFixture<RevokeTrainer>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [RevokeTrainer]
    })
    .compileComponents();

    fixture = TestBed.createComponent(RevokeTrainer);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
