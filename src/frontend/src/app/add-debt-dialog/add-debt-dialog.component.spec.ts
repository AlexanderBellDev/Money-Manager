import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {AddDebtDialogComponent} from './add-debt-dialog.component';

describe('AddDebtDialogComponent', () => {
  let component: AddDebtDialogComponent;
  let fixture: ComponentFixture<AddDebtDialogComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [AddDebtDialogComponent]
    })
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AddDebtDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
