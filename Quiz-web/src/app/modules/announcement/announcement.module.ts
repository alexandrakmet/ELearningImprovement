import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { AnnouncementRoutingModule } from './announcement-routing.module';
import { ViewAnnouncementComponent } from './view-announcement/view-announcement.component';
import {RouterModule} from "@angular/router";
import {FormsModule} from "@angular/forms";
import { UpdateAnnouncementComponent } from './update-announcement/update-announcement.component';
import { AnnouncementsComponent } from './announcements/announcements.component';
import {TranslateModule} from "@ngx-translate/core";
import {SharedModule} from "../shared/shared.module";
import { GroupsComponent } from './groups/groups.component';


@NgModule({
  declarations: [
    ViewAnnouncementComponent,
    UpdateAnnouncementComponent,
    AnnouncementsComponent,
    GroupsComponent],
    imports: [
        CommonModule,
        AnnouncementRoutingModule,
        RouterModule,
        FormsModule,
        TranslateModule,
        SharedModule
    ]
})
export class AnnouncementModule { }
