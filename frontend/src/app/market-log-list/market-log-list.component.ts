import { Component, OnInit, ViewChild } from '@angular/core';
import { MatPaginator, MatSort, MatTableDataSource, MatFormFieldModule } from '@angular/material';
import  {MarketLogEntry} from "../model/market-log-entry";
import { ActivatedRoute } from '@angular/router';
import { MarketLogService} from "../services/market-log.service";

@Component({
  selector: 'app-market-log-list',
  templateUrl: './market-log-list.component.html',
  styleUrls: ['./market-log-list.component.css']
})
export class MarketLogListComponent implements OnInit {

  pretendersFilter = true;

  displayedColumns = ['logoUrl','marketName', 'percent', 'fromStamp', 'toStamp'];
  dataSource: MatTableDataSource<MarketLogEntry>;

  @ViewChild(MatPaginator) paginator: MatPaginator;
  @ViewChild(MatSort) sort: MatSort;

  marketLogEntries: MarketLogEntry[];

  constructor(
    private marketLogService: MarketLogService,
    private route: ActivatedRoute
  ) { }

  ngOnInit() {
    this.getMarketLogEntries(this.pretendersFilter);
  }

  onPretendersFilterChange(event: any) {
    this.pretendersFilter = event;
    this.getMarketLogEntries(this.pretendersFilter);
  }

  applyFilter(filterValue: string) {
    filterValue = filterValue.trim(); // Remove whitespace
    filterValue = filterValue.toLowerCase(); // Datasource defaults to lowercase matches
    this.dataSource.filter = filterValue;
  }

  getMarketLogEntries(pretendersFilter: boolean){
    this.marketLogService.getMarketLogEntries(4, this.pretendersFilter)
      .subscribe(marketLogEntries => {
      this.marketLogEntries = marketLogEntries;
      this.dataSource = new MatTableDataSource(this.marketLogEntries);
      this.dataSource.paginator = this.paginator;
      this.dataSource.sort = this.sort;
    });
  }

}
