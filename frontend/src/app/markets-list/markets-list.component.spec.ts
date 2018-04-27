import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { MarketsListComponent } from './markets-list.component';

describe('MarketsListComponent', () => {
  let component: MarketsListComponent;
  let fixture: ComponentFixture<MarketsListComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ MarketsListComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(MarketsListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
