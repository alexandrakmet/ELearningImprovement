import {Component, HostListener, OnInit} from '@angular/core';
import {Role} from "../../core/models/role";
import {SecurityService} from "../../core/services/security.service";
import {ProfileService} from "../../core/services/profile.service";
import {User} from "../../core/models/user";
import {TranslateService} from "@ngx-translate/core";
import {registerLocaleData} from "@angular/common";
import localeUa from "@angular/common/locales/uk";
import localeEnGb from "@angular/common/locales/en-GB";

@Component({
  selector: 'app-userlist',
  templateUrl: './user-list.component.html',
  styleUrls: ['./user-list.component.css']
})
export class UserListComponent implements OnInit {

  filter: string = "";
  users: User[] = [];
  isWaiting: boolean = false;
  role: Role;
  roleEnum = Role;
  allUsers: boolean;
  private keyUpTimeout: any;

  constructor(private profileService: ProfileService,
              private securityService: SecurityService,
              public translate: TranslateService
  ) {
    this.role = this.securityService.getCurrentRole();
  }

  @HostListener("window:scroll", ["$event"])
  onWindowScroll() {
    if (document.documentElement.scrollHeight - document.documentElement.scrollTop -
      document.documentElement.clientHeight < 40) {
      if (this.users.length % 10 == 0) this.getNew();
    }
  }

  getNew(): void {
    if (this.isWaiting) {
      return;
    }
    this.isWaiting = true;
    this.allUsers = (this.role != Role.USER);
    this.profileService.getFilterUsers(this.users.length, this.allUsers, this.filter)
      .subscribe(
        users => {
          if (users.length == 0) {
            this.isWaiting = false;
            return;
          }
          this.isWaiting = false;
          this.users = this.users.concat(users);
        },
        err => {
          console.log(err);
        })

  }

  ngOnInit(): void {
    this.getNew();

    registerLocaleData(localeUa, 'ua');
    registerLocaleData(localeEnGb, 'en-GB');
  }

  onKeyUp(event: Event) {
    event.stopPropagation();
    clearTimeout(this.keyUpTimeout);
    this.keyUpTimeout = setTimeout(() => {
      this.users = [];
      this.getNew();
    }, 450);
  }


}
