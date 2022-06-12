import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from "@angular/router";
import {SecurityService} from "../../core/services/security.service";
import {GameDto} from "../../core/models/gameDto";
import {PlayGameService} from "../../core/services/play-game.service";
import {TranslateService} from "@ngx-translate/core";

@Component({
  selector: 'app-settings',
  templateUrl: './settings.component.html',
  styleUrls: ['./settings.component.css']
})
export class SettingsComponent implements OnInit {
  game: GameDto;

  message: string = "";
  isInvalid: boolean = false;

  constructor(private route: ActivatedRoute,
              private redirect: Router,
              private securityService: SecurityService,
              private playGameService: PlayGameService,
              public translate: TranslateService
  ) {
    this.game = new GameDto();
    this.game.quizId = this.route.snapshot.paramMap.get('quizId');
    this.game.hostId = securityService.getCurrentId();

  }

  ngOnInit(): void {
  }

  isValid(): boolean {
    if (this.game.time) {
      if (this.game.time < 61 && this.game.time > 4) return true;
      else {
        this.isInvalid = true;
        this.message = "Час на відповідь має бути заповненим та знаходитись в межах від 5 до 60";
        return false;
      }
    } else {
      this.game.time = 15;
      return true;
    }
  }

  submit() {
    if (this.isValid()) {
      this.playGameService.sendGame(this.game).subscribe(
        gameId => {
          this.redirect.navigate(['game/' + gameId + '/play']);
        }, err => {
          console.log(err);
          this.redirect.navigate(['quiz/' + this.game.quizId]);
        });

    }
  }
}
