import {Component, OnInit} from '@angular/core';
import {SecurityService} from "../../core/services/security.service";
import {User} from "../../core/models/user";
import {TranslateService} from "@ngx-translate/core";
import {registerLocaleData} from "@angular/common";
import localeUa from "@angular/common/locales/uk";
import localeEnGb from "@angular/common/locales/en-GB";
import {Group} from "../../core/models/group";
import {GroupService} from "../../core/services/group.service";

@Component({
  selector: 'app-groups',
  templateUrl: './groups.component.html',
  styleUrls: ['./groups.component.css']
})
export class GroupsComponent implements OnInit {

  id: number;
  public creation: boolean = false;
  public joining: boolean = false;
  isWaiting: boolean = false;
  joinCode: string;

  public groups: Array<Group>;
  public group: Group = new Group();

  constructor(private groupService: GroupService,
              private securityService: SecurityService,
              public translate: TranslateService) {
  }

  ngOnInit(): void {
    this.isWaiting = true;

    this.id = this.securityService.getCurrentId();
    this.groupService.getUserGroups(this.id)
      .subscribe(
        groups => {
          this.isWaiting = false;
          this.groups = groups;
        })

    registerLocaleData(localeUa, 'ua');
    registerLocaleData(localeEnGb, 'en-GB');
  }

  createChat() {
    this.group.authorId = this.id;
    this.groupService.createGroup(this.group, this.id)
      .subscribe(
        data => {
          this.creation = false;
          window.location.reload();
        }
      )
  }

  join() {
    this.groupService.inviteToGroup(this.securityService.getCurrentId(), this.joinCode)
      .subscribe(
        data => {
          window.location.reload();
        }
      );
    //window.location.reload();
  }
}
