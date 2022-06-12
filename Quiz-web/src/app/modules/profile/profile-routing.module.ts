import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {ViewProfile} from "./view-profile/view-profile.component";
import {EditorComponent} from "./editor/editor.component";
import {AuthGuardService as AuthGuard} from "../core/services/auth-guard.service";
import {UserListComponent} from "./user-list/user-list.component";
import {FriendListComponent} from "./friend-list/friend-list.component";
import {TopUserListComponent} from "./top-user-list/top-user-list.component";

const routes: Routes = [

  {path: 'profile', component: ViewProfile, canActivate: [AuthGuard]},
  {path: 'edit', component: EditorComponent, canActivate: [AuthGuard]},
  {path: 'friends', component: FriendListComponent, canActivate: [AuthGuard]},
  {path: 'users', component: UserListComponent, canActivate: [AuthGuard]},
  {path: 'users/:userId', component: ViewProfile, canActivate: [AuthGuard]},
  {path: 'top', component: TopUserListComponent, canActivate: [AuthGuard]}
];


@NgModule({
  declarations: [],
  imports: [
    RouterModule.forChild(routes)
  ],
  exports: [
    RouterModule
  ]
})
export class ProfileRoutingModule {
}
