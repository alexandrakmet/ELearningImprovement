import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {AuthGuardService as AuthGuard} from "../core/services/auth-guard.service";
import {ChatAreaComponent} from "./chat-area/chat-area.component";
import {ChatListComponent} from "./chat-list/chat-list.component";

const chatRoutes: Routes = [
  {path: 'chats', component: ChatListComponent, canActivate: [AuthGuard]},
  {path: 'chat/:chatId', component: ChatAreaComponent, canActivate: [AuthGuard]},
];

@NgModule({
  declarations: [],
  imports: [
    RouterModule.forChild(chatRoutes)
  ],
  exports: [
    RouterModule
  ]
})
export class ChatRoutingModule {
}
