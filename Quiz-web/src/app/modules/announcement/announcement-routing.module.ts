import {NgModule} from '@angular/core';
import {Routes, RouterModule} from '@angular/router';
import {ViewAnnouncementComponent} from "./view-announcement/view-announcement.component";
import {UpdateAnnouncementComponent} from "./update-announcement/update-announcement.component";
import {AnnouncementsComponent} from "./announcements/announcements.component";
import {UpdateQuizComponent} from "../quiz/update-quiz/update-quiz.component";
import {AuthGuardService as AuthGuard} from "../core/services/auth-guard.service";
import {GroupsComponent} from "./groups/groups.component";


const routes: Routes = [
  {path: 'announcement/:announcementId', component: ViewAnnouncementComponent, canActivate: [AuthGuard]},
  {path: 'announcement/:announcementId/edit', component: UpdateAnnouncementComponent, canActivate: [AuthGuard]},
  {path: 'createAnnouncement', component: UpdateAnnouncementComponent, canActivate: [AuthGuard]},
  {path: 'groups', component: GroupsComponent, canActivate: [AuthGuard]},
  {path: 'group/:groupId', component: AnnouncementsComponent, canActivate: [AuthGuard]}
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class AnnouncementRoutingModule {
}
