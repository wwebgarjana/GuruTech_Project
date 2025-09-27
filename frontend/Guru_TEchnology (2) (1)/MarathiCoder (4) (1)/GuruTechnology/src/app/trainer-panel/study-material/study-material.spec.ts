import { ComponentFixture, TestBed } from '@angular/core/testing';

import { StudyMaterial } from './study-material';

describe('StudyMaterial', () => {
  let component: StudyMaterial;
  let fixture: ComponentFixture<StudyMaterial>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [StudyMaterial]
    })
    .compileComponents();

    fixture = TestBed.createComponent(StudyMaterial);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
