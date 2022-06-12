import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {ProfileService} from "../../core/services/profile.service";
import {Role} from "../../core/models/role";
import {Router} from "@angular/router";
import {SecurityService} from "../../core/services/security.service";
import {AuthenticationService} from "../../core/services/authentication.service";
import {TranslateService} from '@ngx-translate/core';
import {Lang} from "../../core/models/lang";

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css']
})
export class HeaderComponent implements OnInit {
  id: number;
  roleEnum= Role;
  @Input()
  showNotification: boolean;
  @Input()
  showMessage: boolean;
  @Input() isOpened: boolean;
  @Output()
  public buttonClicked: EventEmitter<any> = new EventEmitter();

  constructor(
    private profileService: ProfileService,
    public securityService: SecurityService,
    private redirect: Router,
    public authService: AuthenticationService,
    public translate: TranslateService
  ) {
    this.showNotification = false;
    this.showMessage = false;
  }

  ngOnInit(): void {
  }

  getMessage(showMessage: boolean) {
    if (showMessage) {
      if (this.showNotification) {
        this.showNotification = false;
      }
    }
    this.showMessage = showMessage;
  }

  getNotification(showNotification: boolean) {
    if (showNotification) {
      if (this.showMessage) {
        this.showMessage = !this.showMessage;
      }
    }
    this.showNotification = showNotification;
  }

  setLang(lang: string) {
    this.translate.use(lang)
    let id = this.securityService.getCurrentId();
    if (id) {
      this.authService.setLang(id, Lang[lang]).subscribe(_ => {
      });
    }
  }

  search(event: any) {
    this.redirect.navigate(['/quizzes'], {queryParams: {quizName: event.target.value}});
  }

  logout() {
    this.authService.logOut();
    this.redirect.navigate(['home']);
  }

  clicked() {
    this.buttonClicked.emit();
  }
}
