import {Component, OnInit} from '@angular/core';
import {ProfileService} from "../../core/services/profile.service";
import {Role} from "../../core/models/role";
import {SecurityService} from "../../core/services/security.service";
import {AuthenticationService} from "../../core/services/authentication.service";
import {TranslateService} from "@ngx-translate/core";
@Component({
  selector: 'app-sidenav',
  templateUrl: 'sidenav.component.html',
  styleUrls: ['./sidenav.component.css']
})
export class SidenavComponent implements OnInit {
  id: number;
  role: String;
  roleEnum = Role;
  isUser: boolean;
  isAdmin: boolean;
  notProfile: boolean;

  constructor(
    private profileService: ProfileService,
    public securityService: SecurityService,
    public authenticationService: AuthenticationService,
    public translate: TranslateService,
  ) {

  }


  ngOnInit(): void {
    this.setCondition(null);
    //this.getUser();

  }

  private getUser() {
    this.id = this.securityService.getCurrentId();
    this.role = this.securityService.getCurrentRole();
    this.profileService.getUser(this.id).subscribe(data => {
      this.setCondition(data.role);
    });
  }

  private setCondition(role: String) {
    if (role == null) {
      this.notProfile = true;
    } else if (role == Role.USER) {
      this.isUser = true;
    } else if (role == Role.ADMIN || role == Role.MODERATOR || role == Role.SUPER_ADMIN) {
      this.isAdmin = true;
    }
  }


  onScrollClick() {
    window.scroll(0, 0);
  }
}
