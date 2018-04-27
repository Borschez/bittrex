import {Component, OnInit, Input} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {MarketService} from '../services/market.service';
import {Market} from '../model/market';
import {Location} from '@angular/common';
import {DatePipe} from '@angular/common';

import {Chart} from 'chart.js';

@Component({
  selector: 'app-market',
  templateUrl: './market.component.html',
  styleUrls: ['./market.component.css']
})
export class MarketComponent implements OnInit {
  @Input() market: Market;

  chart = []; // This will hold our chart info

  constructor(private route: ActivatedRoute,
              private marketService: MarketService,
              private location: Location) {
  }

  ngOnInit() {
    this.getMarket();
  }

  getMarket(): void {
    const id = +this.route.snapshot.paramMap.get('id');
    this.marketService.getMarket(id)
      .subscribe(market => {
        this.market = market;

        this.marketService.getTickers(id)
          .subscribe(tickers => {
            this.market.tickers = tickers;
            let stampDates = [];
            let lastData = [];
            let askData = [];
            let backgroundColors = [];
            let datePipe = new DatePipe('ru');
            let previous = null;
            let min = null;
            let max = null;
            tickers.forEach((res) => {
              stampDates.push(datePipe.transform(res.timeStamp, "short"));
              if ((min == null || min > res.last) && res.last !== 0) min = res.last;
              if (max == null || max < res.last) max = res.last;
              let dataItem = res.last - ((tickers.indexOf(res) > 0) ? previous : res.last);
              lastData.push(res.last);
              if (dataItem > 0) {
                backgroundColors.push("#18ba1e");
              } else {
                backgroundColors.push("#ba444a");
              }
              askData.push(res.ask);
              previous = res.last;
            });

            console.log("min: "+min);
            console.log("max: "+max);

            this.chart = new Chart('canvas', {
              type: 'line',
              data: {
                labels: stampDates,
                datasets: [
                  {
                    label: "Last",
                    data: lastData,
                    backgroundColor: "#5f6bba",
                    borderColor: "#5f6bba",
                    yAxisID: 'y-axis-1',
                    fill: false,
                  }
                ]
              },
              options: {
                legend: {
                  display: true
                },
                scales: {
                  yAxes: [{
                    type: 'linear', // only linear but allow scale type registration. This allows extensions to exist solely for log scale for instance
                    display: true,
                    position: 'left',
                    id: 'y-axis-1',
                    ticks: { min: min, max: max , }
                  }],
                }
              }
            });
          });
      });
  }

  goBack(): void {
    this.location.back();
  }

}
