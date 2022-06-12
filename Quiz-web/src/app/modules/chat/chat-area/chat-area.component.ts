import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from "@angular/router";

import {SecurityService} from "../../core/services/security.service";
import {ChatService} from "../../core/services/chat.service";
import {ProfileService} from "../../core/services/profile.service";
import {TranslateService} from "@ngx-translate/core";

import {User} from "../../core/models/user";
import {Chat} from "../../core/models/chat";

@Component({
  selector: 'app-chat-area',
  templateUrl: './chat-area.component.html',
  styleUrls: ['./chat-area.component.css']
})
export class ChatAreaComponent implements OnInit {
  id: number;
  chat: Chat = new Chat();

  public invitation: boolean = false;
  public isLoaded: boolean = false;
  selectedFriend: User;
  friends: User[] = [];

  constructor(private chatService: ChatService,
              private securityService: SecurityService,
              private route: ActivatedRoute,
              private router: Router,
              private profileService: ProfileService,
              public translate: TranslateService) {
  }

  ngOnInit(): void {
    this.id = this.securityService.getCurrentId();
    this.chat.id = +this.route.snapshot.paramMap.get('chatId');

    this.chatService.checkChatAffiliation(this.id, this.chat.id)
      .subscribe(
        answer => {
          if (!answer) {
            this.router.navigate([`chats`]).then();
          }
        }
      );

    this.chatService.getChat(this.chat.id)
      .subscribe(
        chat => {
          this.chat = chat;
        }
      );
  }

  updateChat() {
    this.chatService.updateChat(this.chat).subscribe();
  }

  leaveChat() {
    this.chatService.leaveChat(this.id, this.chat.id)
      .subscribe(
        data => {
          this.router.navigate([`chats`]).then();
        }
      )
  }

  loadFriends() {
    if (!this.isLoaded) {
      this.profileService.getFriends(this.id)
        .subscribe(
          friends => {
            this.friends = friends;
            this.isLoaded = true;
          })
    }
  }

  inviteFriend() {
    if (this.selectedFriend) {
      this.chatService.inviteToChat(this.selectedFriend, this.chat.id)
        .subscribe(
          data => {
            this.invitation = false;
            this.chat.users.push(this.selectedFriend);
          }
        );
    }
  }

  onChange(value: string) {
    this.selectedFriend = this.friends.filter(value1 => value1.login == value)[0];
  }
}
