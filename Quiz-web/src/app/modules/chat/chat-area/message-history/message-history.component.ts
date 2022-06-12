import {Component, ElementRef, HostListener, Input, OnDestroy, OnInit, ViewChild} from '@angular/core';
import {ChatService} from "../../../core/services/chat.service";
import {TranslateService} from "@ngx-translate/core";

import {registerLocaleData} from "@angular/common";
import localeUa from "@angular/common/locales/uk";
import localeEnGb from "@angular/common/locales/en-GB";

import {Message} from "../../../core/models/message";
import {Chat} from "../../../core/models/chat";

import * as Stomp from '@stomp/stompjs';
import * as SockJS from 'sockjs-client';
import {socket} from "../../../../../environments/environment.prod";
import {EventType, WebsocketEvent} from "../../../core/models/websocketEvent";

@Component({
  selector: 'app-message-history',
  templateUrl: './message-history.component.html',
  styleUrls: ['./message-history.component.css']
})
export class MessageHistoryComponent implements OnInit, OnDestroy {
  @Input() chat: Chat;
  @Input() id: number;

  isWaiting: boolean = false;
  isOver: boolean = false;
  isInit: boolean = true;

  public messages: Array<Message> = new Array<Message>();
  message: Message = new Message();

  @ViewChild('msgScroll') private myScrollContainer: ElementRef;

  private stompClient;
  eventType = EventType;
  receivedEvent: WebsocketEvent;
  s: any;

  constructor(private chatService: ChatService,
              public translate: TranslateService) {
  }

  ngOnInit(): void {
    this.getMessages();

    this.initializeWebSocketConnection();

    registerLocaleData(localeUa, 'ua');
    registerLocaleData(localeEnGb, 'en-GB');
  }

  ngOnDestroy(): void {
    this.s.unsubscribe();
    this.stompClient.disconnect();
  }

  scrollToBottom(): void {
    this.myScrollContainer.nativeElement.scrollTop = this.myScrollContainer.nativeElement.scrollHeight;
  }

  initializeWebSocketConnection() {
    let ws = new SockJS(socket);
    this.stompClient = Stomp.Stomp.over(ws);
    let that = this;

    this.stompClient.connect({}, function () {
      that.s = that.stompClient.subscribe("/chat/" + that.chat.id, (message) => {
        if (message.body) {
          that.receivedEvent = JSON.parse(message.body);
          if (that.receivedEvent.type == that.eventType.MESSAGE) {
            that.receivedEvent.message.creationDate = new Date();
            that.messages.push(that.receivedEvent.message);
            setTimeout(() => that.scrollToBottom(), 2);
          }
        }
      });

    }, this);
  }

  sendMessage() {
    if (this.message.messageText.length == 0) {
      return;
    }

    this.message.chatId = this.chat.id;
    this.message.authorId = this.id;

    this.stompClient.send("/chat/" + this.chat.id, {}, JSON.stringify(this.message));
    this.message.messageText = '';
  }

  getMessages(): void {
    if (this.isWaiting) {
      return;
    }
    this.isWaiting = true;

    this.chatService.getMessages(this.chat.id, this.messages.length)
      .subscribe(
        messages => {
          if (messages.length < 10) {
            this.isOver = true;
          }

          if (messages.length == 0) {
            this.isWaiting = false;
            return;
          }
          this.isWaiting = false;
          this.messages = messages.reverse().concat(this.messages);

          if (this.isInit) {
            this.isInit = false;
            setTimeout(() => this.scrollToBottom(), 10);
          } else {
            this.scrollToBottom();
          }

        })
  }

  @HostListener('scroll', ['$event'])
  scrollHandler(event) {
    if (event.target.scrollHeight - event.target.scrollTop - event.target.clientHeight > (event.target.scrollTopMax - 20)) {
      if (!this.isOver) {
        this.getMessages();
      }
    }
  }

}
