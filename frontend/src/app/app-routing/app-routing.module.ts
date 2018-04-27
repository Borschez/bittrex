import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {MarketsListComponent} from '../markets-list/markets-list.component';
import {MarketComponent} from '../market/market.component';

const routes: Routes = [
  { path: 'all-markets', component: MarketsListComponent, data : {maxCount: -1, title: 'All Markets'}},
  { path: 'top-markets', component: MarketsListComponent, data : {maxCount: 10, title: 'Top Markets'}},
  { path: 'market/:id', component: MarketComponent },
];

@NgModule({
  exports: [ RouterModule ],
  imports: [ RouterModule.forRoot(routes) ]
})
export class AppRoutingModule { }
