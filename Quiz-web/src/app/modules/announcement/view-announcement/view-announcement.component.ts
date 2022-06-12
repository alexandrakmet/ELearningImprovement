import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from "@angular/router";
import {AnnouncementService} from "../../core/services/announcement.service";
import {Announcement} from "../../core/models/announcement";
import {Role} from "../../core/models/role";
import {SecurityService} from "../../core/services/security.service";

@Component({
  selector: 'app-view-announcement',
  templateUrl: './view-announcement.component.html',
  styleUrls: ['./view-announcement.component.css']
})
export class ViewAnnouncementComponent implements OnInit {

  announcement: Announcement;
  role: Role;
  roleEnum = Role;

  constructor(
    private announcementService: AnnouncementService,
    private route: ActivatedRoute,
    private redirect: Router,
    private securityService: SecurityService) {
    this.role = this.securityService.getCurrentRole();
    const id = this.route.snapshot.paramMap.get('announcementId');
    console.log(id);
    if (id) {
      this.announcementService.getById(id).subscribe(
        data => {
          this.announcement = data;
        }, err => {
          redirect.navigate(['announcements']);
        });
    } else {
      redirect.navigate(['announcements']);
    }

  }

  ngOnInit(): void {
  }


  publish() {
    this.announcement.isPublished = !this.announcement.isPublished;
    this.announcementService.updateAnnouncement(this.announcement).subscribe(
      get => {
      },
      error => {
        console.log(error);
      });
  }

  delete() {
    this.announcementService.deleteAnnouncement(this.announcement).subscribe(
      get => {
        this.redirect.navigate(['announcements']);
      },
      error => {
        console.log(error);
      });
  }
}
