import { Component } from '@angular/core';
import {TranslateService} from "@ngx-translate/core";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'web';
  isOpened: boolean = false;

  constructor(public translate: TranslateService) {
    translate.addLangs(['en', 'ua']);
    translate.setDefaultLang('en');

    const browserLang = translate.getBrowserLang();
    translate.use(browserLang.match(/en|ua/) ? browserLang : 'en');
  }


  onSidebarClick() {
    this.isOpened = !this.isOpened;
  }
  greeting : any;
  handleMessage(message){
    this.greeting = message;
  }
}
