import {Component, HostListener, OnInit} from '@angular/core';
import {Quiz} from "../../core/models/quiz";
import {countOnPage} from "../../../../environments/environment.prod";
import {QuizzesService} from "../../core/services/quizzes.service";
import {SecurityService} from "../../core/services/security.service";

@Component({ 
  selector: 'app-user-quizzes',
  templateUrl: './user-quizzes.component.html',
  styleUrls: ['./user-quizzes.component.css']
})
export class UserQuizzesComponent implements OnInit {

  constructor(private quizzesService: QuizzesService,
              private securityService: SecurityService) {
    this.quizzes=[];
    for(let i=0;i<3;i++){
      this.quizzes[i]=[];
    }
  }

  ngOnInit(): void {
    this.getQuizzes(0);//favorite
    this.getQuizzes(1);//completed
    this.getQuizzes(2);//created
  }

  quizzes: Quiz[][];
  selectedTab: number = 0;
  isWaiting: boolean[] = [];

  @HostListener("window:scroll", ["$event"])
  onWindowScroll() {
    if (document.documentElement.scrollHeight - document.documentElement.scrollTop -
      document.documentElement.clientHeight < 40) {
      this.getQuizzes(this.selectedTab);
    }
  }

  getQuizzes(type: number) {
    if (this.isWaiting[type]) {
      return;
    }
    this.isWaiting[type] = true;
    this.quizzesService.getUserQuizzes(type, this.quizzes[type].length, this.securityService.getCurrentId())
      .subscribe(
        quizzes => {
          if (quizzes.length == countOnPage) {
            this.isWaiting[type] = false;
          }
          this.quizzes[type] = this.quizzes[type].concat(quizzes);
        },
        err => {
          console.log(err);
        })
  }

}
