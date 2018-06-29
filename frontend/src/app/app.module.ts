import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { HTTP_INTERCEPTORS, HttpClientModule } from '@angular/common/http';
import { FormsModule } from '@angular/forms';
import localeRu from '@angular/common/locales/ru';
import { registerLocaleData } from '@angular/common';

import { AppComponent } from './app.component';
import { MarketsListComponent } from './markets-list/markets-list.component';
import { MarketService } from './services/market.service';
import { MarketLogService } from "./services/market-log.service";
import { AppRoutingModule } from './app-routing/app-routing.module';
import { CommonService } from './services/common.service';
import { MarketComponent } from './market/market.component';
import { MatFormFieldModule, MatInputModule, MatTableModule, MatPaginatorModule, MatSortModule } from '@angular/material';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { WebSocketService } from "./services/web-socket.service";
import { PushNotificationModule } from 'ng-push-notification';
import { DecimalPipe } from "@angular/common";
import { DatePipe } from "@angular/common";
import { MarketLogListComponent } from './market-log-list/market-log-list.component';
import { SettingsComponent } from './settings/settings.component';
import {SettingsService} from "./services/settings.service";

@NgModule({
  declarations: [
    AppComponent,
    MarketsListComponent,
    MarketComponent,
    MarketLogListComponent,
    SettingsComponent
  ],
  imports: [
    BrowserModule,
    FormsModule,
    AppRoutingModule,
    HttpClientModule,
    MatFormFieldModule,
    MatInputModule,
    MatTableModule,
    MatPaginatorModule,
    BrowserAnimationsModule,
    MatSortModule,
    PushNotificationModule.forRoot()
  ],
  providers: [MarketService, CommonService, WebSocketService, DecimalPipe, DatePipe, MarketLogService, SettingsService],
  bootstrap: [AppComponent]
})

export class AppModule { }

registerLocaleData(localeRu, 'ru');
