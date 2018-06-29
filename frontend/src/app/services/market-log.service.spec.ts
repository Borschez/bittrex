import { TestBed, inject } from '@angular/core/testing';

import { MarketLogService } from './market-log.service';

describe('MarketLogService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [MarketLogService]
    });
  });

  it('should be created', inject([MarketLogService], (service: MarketLogService) => {
    expect(service).toBeTruthy();
  }));
});
