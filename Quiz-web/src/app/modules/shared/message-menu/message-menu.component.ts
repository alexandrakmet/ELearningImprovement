import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {WebsocketEvent} from "../../core/models/websocketEvent";
import {SecurityService} from "../../core/services/security.service";
import {NotificationService} from "../../core/services/notification.service";
import {socket} from "../../../../environments/environment.prod";
import * as Stomp from "@stomp/stompjs";
import * as SockJS from 'sockjs-client';
import {NotificationDto} from "../../core/models/notificationDto";

@Component({
  selector: 'app-message-menu',
  templateUrl: './message-menu.component.html',
  styleUrls: ['./message-menu.component.css']
})
export class MessageMenuComponent implements OnInit {
  public stompClient;
  messages = [];
  unviewedNotificationCount = 0;
  receivedEvent: WebsocketEvent;
  userId: number;

  @Input()
  showMessage: boolean;

  constructor(public securityService: SecurityService,
              private notificationService: NotificationService) {
  }

  getNotificationFromDB() {
    this.notificationService.getMessagesByUserId(this.securityService.getCurrentId()).subscribe(
      notifications => {
        console.log(notifications);
        this.messages = this.messages.concat(notifications);
        for (let i = 0; i < this.messages.length; i++) {
          if (!this.messages[i].isViewed) {
            this.unviewedNotificationCount++;
          }
        }
      },
      err => {
        console.log(err);
      });
  }

  initializeWebSocketConnection() {
    let ws = new SockJS(socket);
    this.stompClient = Stomp.Stomp.over(ws);
    let that = this;
    let href: string;
    this.stompClient.connect({}, function () {
      that.stompClient.subscribe("/notification" + that.securityService.getCurrentId(), async (message) => {
        if (message.body) {
          that.receivedEvent = JSON.parse(message.body);
          if (that.receivedEvent.notification.isMessage) {
            href = window.location.href;
            if (!href.includes(that.receivedEvent.notification.actionLink)) {
              that.messages.push(that.receivedEvent.notification);
              that.unviewedNotificationCount++;
            } else if (href.includes(that.receivedEvent.notification.actionLink)) {
              that.deleteNotification(that.receivedEvent.notification.actionLink, that.receivedEvent.notification.id);
            }
          }
        }
      });
    }, this);
  }

  ngOnInit(): void {
    this.initializeWebSocketConnection();
    this.getNotificationFromDB();
  }

  setViewed(notification: NotificationDto) {
    if (!notification.isViewed) {
      notification.isViewed = true;
      this.notificationService.updateNotificationView(notification).subscribe();
      if (this.unviewedNotificationCount > 0) {
        this.unviewedNotificationCount--;
      }
    }
  }

  deleteNotification(linkAction: string, id: number) {
    this.messages.splice(this.messages.indexOf(linkAction), 1);
    this.notificationService.deleteNotificationById(id).subscribe();
  }

  deleteAllNotification() {
    this.messages = [];
    this.unviewedNotificationCount = 0;
    this.notificationService.deleteAllByUserId(this.securityService.getCurrentId()).subscribe();
  }

  @Output() isMessage = new EventEmitter<boolean>();

  changeShowMenu(isMessage: boolean) {
    this.isMessage.emit(isMessage);
  }

  openNotification(state: boolean) {
    this.showMessage = state;
  }
}
