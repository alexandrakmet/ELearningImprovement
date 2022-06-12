import {NgModule} from '@angular/core';
import {CommonModule} from '@angular/common';
import {RouterModule} from '@angular/router';
import {FormsModule} from "@angular/forms";

import {ViewProfile} from './view-profile/view-profile.component'
import {EditorComponent} from './editor/editor.component'
import {ProfileRoutingModule} from './profile-routing.module';
import { UserListComponent } from './user-list/user-list.component'
import {AchievementModule} from "../achivement/achievement.module";
import { FriendListComponent } from './friend-list/friend-list.component';
import {TranslateModule} from "@ngx-translate/core";
import {TopUserListComponent} from "./top-user-list/top-user-list.component";
import {SharedModule} from "../shared/shared.module";
import {AlertComponent} from "../shared/alert/alert.component";
import {HeaderComponent} from "../shared/header/header.component";
import {FooterComponent} from "../shared/footer/footer.component";
import {SidenavComponent} from "../shared/sidenav/sidenav.component";
import {SpinCatComponent} from "../shared/spincat/spin-cat.component";

@NgModule({
  declarations: [
    ViewProfile,
    EditorComponent,
    UserListComponent,
    FriendListComponent,
    TopUserListComponent
  ],
    imports: [
        CommonModule,
        ProfileRoutingModule,
        RouterModule,
        FormsModule,
        AchievementModule,
        TranslateModule,
        SharedModule
    ],
  exports: [
    TopUserListComponent
  ]
})
export class ProfileModule { }
