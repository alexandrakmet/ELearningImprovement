import {Component, OnInit} from '@angular/core';
import {Router} from '@angular/router';
import {NotificationMenuComponent} from "../../shared/notification-menu/notification-menu.component";
import {AuthenticationService} from '../../core/services/authentication.service';
import {AlertService} from '../../core/services/alert.service';
import {TranslateService} from "@ngx-translate/core";

import {User} from '../../core/models/user';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

  constructor(
    private authenticationService: AuthenticationService,
    private notificationMenuComponent: NotificationMenuComponent,
    private alertService: AlertService,
    public translate: TranslateService,
    private router: Router
  ) {
  }

  ngOnInit(): void {
  }

  onLogin(login: string, password: string) {
    login = login.trim();
    if (!login) {
      this.alertService.error('alert.loginEmpty');
      return;
    }

    this.authenticationService.login({login, password} as User)
      .subscribe(
        data => {
          this.router.navigate(['profile']).then();
        },
        error => {
          this.alertService.error('alert.' + error.error.message);
        });
  }

}
