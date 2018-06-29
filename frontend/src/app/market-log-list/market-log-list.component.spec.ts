import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { MarketLogListComponent } from './market-log-list.component';

describe('MarketLogListComponent', () => {
  let component: MarketLogListComponent;
  let fixture: ComponentFixture<MarketLogListComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ MarketLogListComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(MarketLogListComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
