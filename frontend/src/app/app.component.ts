import { Component } from '@angular/core';
import { WebSocketService } from "./services/web-socket.service";
import { PushNotificationService } from 'ng-push-notification';
import { DecimalPipe } from "@angular/common";
import { DatePipe } from '@angular/common';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'Markets';

  constructor(private webSocketService: WebSocketService,
              private pushNotification: PushNotificationService,
              private decimalPipe: DecimalPipe,
              private datePipe: DatePipe,
              ) {

    pushNotification.requestPermission();

    // Open connection with server socket
    let stompClient = this.webSocketService.connect();
    stompClient.connect({}, frame => {

      // Subscribe to notification topic
      stompClient.subscribe('/topic/notification', logEntry => {

        let response = JSON.parse(logEntry.body)

        this.showPush(response);
      })
    });
  }

  showPush(response: any) {
    let toStamp:Date = response.toStamp;
    let settings = {
      icon: response.marketLogoUrl,
      body: this.datePipe.transform(toStamp, "shortTime","+0300" ,"ru-RU")+" "+this.decimalPipe.transform(response.percent, "2.3-3")+" % ",
      sound: 'audio/alert.mp3'
    };
    this.pushNotification.show(
      response.market,
      settings,
      null, // close delay.
    );
  }
}
