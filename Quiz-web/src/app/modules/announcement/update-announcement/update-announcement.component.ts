import {Component, OnInit} from '@angular/core';
import {Announcement} from "../../core/models/announcement";
import {ActivatedRoute, Router} from "@angular/router";
import {AnnouncementService} from "../../core/services/announcement.service";
import {Imaged} from "../../core/models/imaged";
import {Image} from "../../core/models/image";
import {SecurityService} from "../../core/services/security.service";
import {Role} from "../../core/models/role";
import {TranslateService} from "@ngx-translate/core";

@Component({
  selector: 'app-update-announcement',
  templateUrl: './update-announcement.component.html',
  styleUrls: ['../../quiz/update-quiz/update-quiz.component.html']
})
export class UpdateAnnouncementComponent implements OnInit {

  announcement: Announcement;
  updated: boolean = false;
  isInvalid: boolean = false;
  //message: string;

  constructor(private announcementService: AnnouncementService,
              private route: ActivatedRoute,
              private securityService: SecurityService,
              private router: Router,
              public translate: TranslateService
  ) {
    const id = this.route.snapshot.paramMap.get('announcementId');
    console.log(id);
    if (id) {
      this.announcementService.getById(id).subscribe(
        data => {
          this.announcement = data;
        }, err => {
          console.log(err);
          this.announcement = new Announcement();
        });
    } else {
      this.announcement = new Announcement();
      this.announcement.image = new Image();
      this.announcement.authorId = this.securityService.getCurrentId();
    }

  }

  ngOnInit(): void {
  }

  processFile(imageInput: any, imaged: Imaged) {
    const file: File = imageInput.files[0];
    const reader = new FileReader();

    reader.addEventListener('load', (event: any) => {
      console.log("in reader.addEventListener (announcement)")
      imaged.image.src = event.target.result;
      this.updated = true;
      this.announcementService.putImage(file).subscribe(
        id => {
          console.log("id = " + id);
          if (typeof id === "number") {
            imaged.imageId = id;
          }
        },
        error => {
          console.log(error);
        });
    });
    reader.readAsDataURL(file);
  }

  isValid(): boolean {

    if (this.announcement.title && this.announcement.fullText) return true;
    else {
      this.isInvalid = true;
      return false;
    }
  }

  send() {
    if (this.isValid()) {
      if (this.announcement.id) {
        this.announcementService.updateAnnouncement(this.announcement).subscribe(
          get => {
            console.log("id = " + get);
          },
          error => {
            console.log(error);
          });
      } else {
        this.announcement.isPublished = (this.securityService.getCurrentRole() != Role.USER);
        this.announcementService.sendAnnouncement(this.announcement).subscribe(
          get => {
            console.log("id = " + get);
            if (this.announcement.isPublished) this.router.navigate(["announcement/" + get]);
          },
          error => {
            console.log(error);
          });
      }
      this.router.navigate(["groups"]);
    }
  }
}
