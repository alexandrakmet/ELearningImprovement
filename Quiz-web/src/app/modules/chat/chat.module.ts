import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {RouterModule} from "@angular/router";
import {SharedModule} from "../shared/shared.module";
import {ChatListComponent} from './chat-list/chat-list.component';
import {ChatAreaComponent} from './chat-area/chat-area.component';
import {FormsModule} from "@angular/forms";
import {CoreModule} from "../core/core.module";
import { MessageHistoryComponent } from './chat-area/message-history/message-history.component';


@NgModule({
  declarations: [ChatListComponent, ChatAreaComponent, MessageHistoryComponent],
  imports: [
    CommonModule,
    RouterModule,
    SharedModule,
    FormsModule,
    CoreModule
  ],
})
export class ChatModule {
}
