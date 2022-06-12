import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {Question} from '../../../core/models/question';

import {timer} from 'rxjs';
import {Answer} from "../../../core/models/answer";

import {TranslateService} from "@ngx-translate/core";

@Component({
  selector: 'app-play-question',
  templateUrl: './play-question.component.html',
  styleUrls: ['./play-question.component.css']
})
export class PlayQuestionComponent implements OnInit {
  public isSend: boolean = false;

  public subscribeTimer: number;

  @Input()
  timeLeft: number;

  @Input()
  question: Question;

  @Input() questionNumber: number;
  @Input() questionsCount: number;

  @Output()
  public sendAnswer: EventEmitter<any> = new EventEmitter();

  answer: Answer = new Answer();

  sequence: Map<number, number> = new Map();
  answerText = '';

  constructor(public translate: TranslateService) {

    timer(1000, 1000).subscribe(val => {
      if (this.subscribeTimer != 0) {
        this.subscribeTimer = this.timeLeft - val;
      } else {
        if (!this.isSend) {
          this.nextQuestion();
          this.isSend = true;
        }
      }
    });
  }

  ngOnInit(): void {
    let key = 'endTime' + this.question.id;

    if (!localStorage.getItem(key)) {
      localStorage.setItem(key, '' + (new Date().getTime() + this.timeLeft * 1000));
    } else {
      this.timeLeft = Math.round((+localStorage.getItem(key) - new Date().getTime()) / 1000);
    }
  }

  nextQuestion() {
    if (this.question.type == 'ENTER_ANSWER') {
      this.answer.fullAnswer = this.answerText;
    }

    if (this.question.type == 'SELECT_SEQUENCE') {
      this.sequence.forEach((val: number, key: number) => {
        this.answer.sequence[key] = val;
      });
    }

    this.answer.questionId = this.question.id;
    this.isSend = true;

    this.sendAnswer.emit(this.answer)
  }

  setOption(optId: number) {
    if (this.answer.options.indexOf(optId) == -1) {
      this.answer.options.push(optId);
    } else {
      this.answer.options.splice(this.answer.options.indexOf(optId), 1);
    }
  }

  setTrueFalseOption(value: boolean) {
    this.answer.trueFalse = value;
  }

  setSequence(opId: number, seqId: string) {
    if (this.sequence.has(opId)) {
      this.sequence.delete(opId);
    }
    this.sequence.set(opId, +seqId);
  }

}
