import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MyBatches } from './my-batches';

describe('MyBatches', () => {
  let component: MyBatches;
  let fixture: ComponentFixture<MyBatches>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [MyBatches]
    })
    .compileComponents();

    fixture = TestBed.createComponent(MyBatches);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
