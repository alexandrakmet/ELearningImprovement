import {Component, Input, OnInit} from '@angular/core';
import {Quiz} from "../../core/models/quiz";

@Component({
  selector: 'app-quiz-list',
  templateUrl: './quiz-list.component.html',
  styleUrls: ['./quiz-list.component.css']
})
export class QuizListComponent implements OnInit {

  constructor() { }
  @Input()
  quizzes: Quiz[];

  ngOnInit(): void {
  }

}
