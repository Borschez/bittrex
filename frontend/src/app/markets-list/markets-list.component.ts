import { Component, OnInit, ViewChild } from '@angular/core';
import { Market } from '../model/market';
import { MarketService } from '../services/market.service';
import { ActivatedRoute } from '@angular/router';
import { CommonService } from '../services/common.service';
import { MatPaginator, MatSort, MatTableDataSource, MatFormFieldModule } from '@angular/material';


@Component({
  selector: 'app-markets-list',
  templateUrl: './markets-list.component.html',
  styleUrls: ['./markets-list.component.css']
})
export class MarketsListComponent implements OnInit {
  displayedColumns = ['logoUrl', 'name', 'delta', 'last', 'type'];
  dataSource: MatTableDataSource<Market>;

  @ViewChild(MatPaginator) paginator: MatPaginator;
  @ViewChild(MatSort) sort: MatSort;

  private maxCount: number;
  private title: string;
  private interval;
  private refreshPeriod;

  markets: Market[];

  constructor(
    private marketService: MarketService,
    private commonService: CommonService,
    private route: ActivatedRoute
  ) { }

  ngOnInit() {

    this.route.data.subscribe(data=>{
      this.maxCount = data.maxCount;
      this.title = data.title;
    });

    this.getMarkets();

    this.commonService.getSettings()
      .subscribe( settings =>{
        this.refreshPeriod = settings.refreshPeriod;
        console.log("refresh.period: "+this.refreshPeriod );
        if (this.refreshPeriod  > 3000) {
          this.interval = setInterval(() => {
            this.getMarkets();
          }, this.refreshPeriod );
        }

      });
  }

  ngOnDestroy() {
    if (this.interval) clearInterval(this.interval);
  }

  applyFilter(filterValue: string) {
    filterValue = filterValue.trim(); // Remove whitespace
    filterValue = filterValue.toLowerCase(); // Datasource defaults to lowercase matches
    this.dataSource.filter = filterValue;
  }

  getMarkets(): void{
    this.marketService.getMarkets(this.maxCount)
      .subscribe(markets => {
        this.markets = markets;
        this.dataSource = new MatTableDataSource(this.markets);
        this.dataSource.paginator = this.paginator;
        this.dataSource.sort = this.sort;
      });
  }

  getMarketDelta(market: Market): number{
    return market.marketFunction.percentDelta;
  }

}
