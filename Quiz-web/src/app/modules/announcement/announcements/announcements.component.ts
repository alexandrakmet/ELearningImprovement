import {Component, HostListener, OnInit} from '@angular/core';
import {Announcement} from "../../core/models/announcement";
import {AnnouncementService} from "../../core/services/announcement.service";
import {SecurityService} from "../../core/services/security.service";
import {Role} from "../../core/models/role";
import {TranslateService} from "@ngx-translate/core";
import {registerLocaleData} from "@angular/common";
import localeUa from "@angular/common/locales/uk";
import localeEnGb from "@angular/common/locales/en-GB";
import {User} from "../../core/models/user";
import {DashboardService} from "../../core/services/dashboard.service";
import {Group} from "../../core/models/group";
import {ActivatedRoute, Router} from "@angular/router";
import {GroupService} from "../../core/services/group.service";

@Component({
  selector: 'app-announcements',
  templateUrl: './announcements.component.html',
  styleUrls: ['./announcements.component.css']
})

export class AnnouncementsComponent implements OnInit {
  group: Group;
  id: string;
  userId: number;
  announcements: Announcement[] = [];
  isWaiting: boolean = false;
  role: Role;
  roleEnum = Role;
  isPublished: boolean = true;
  selectedTab: number = 0;

  users: User[] = [];


  getUsers() {

    this.groupService.getGroupMembers(this.id).subscribe(users => {
        this.users = users;
        console.log(users);
      },
      err => {
        console.log(err);
      });
  }



  constructor(private announcementService: AnnouncementService,
              private groupService: GroupService,
              private securityService: SecurityService,
              private dashboardService: DashboardService,
              public translate: TranslateService,
              private route: ActivatedRoute,
              private redirect: Router) {
    this.role = this.securityService.getCurrentRole();
    this.userId = this.securityService.getCurrentId();
    this.id = this.route.snapshot.paramMap.get('groupId');
    const id = this.route.snapshot.paramMap.get('groupId');
    console.log(id);
    if (id) {
      this.groupService.getGroup(id).subscribe(
        data => {
          this.group = data;
          console.log(this.group);
        }, err => {
          redirect.navigate(['groups']);
        });
    } else {
      redirect.navigate(['groups']);
    }
    this.getUsers();

  }

  @HostListener("window:scroll", ["$event"])
  onWindowScroll() {
    if (document.documentElement.scrollHeight - document.documentElement.scrollTop -
      document.documentElement.clientHeight < 40) {
      if (this.announcements.length % 10 == 0) this.getNew();
    }
  }

  getNew(): void {
    if (this.isWaiting) {
      return;
    }
    this.isWaiting = true;
    if (!this.isPublished) this.isPublished = false;
    console.log(this.isPublished);
    //this.isPublished = !(this.role && this.role != Role.USER);
    this.announcementService.getAnnouncementsByRole(this.announcements.length, this.isPublished)
      .subscribe(
        announcements => {
          if (announcements.length == 0) {
            return;
          }
          this.isWaiting = false;
          this.announcements = this.announcements.concat(announcements);
        },
        err => {
          console.log(err);
        });


  }

  ngOnInit(): void {
    this.getNew();

    registerLocaleData(localeUa, 'ua');
    registerLocaleData(localeEnGb, 'en-GB');
  }


  getUnpublished() {
    this.announcements = [];
    this.isPublished = false;
    this.getNew();
  }

  getPublished() {
    this.announcements = [];
    this.isPublished = true;
    this.getNew();
  }
}
