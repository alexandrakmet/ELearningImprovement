import {Component, ElementRef, OnInit, ViewChild} from '@angular/core';
import {Category} from "../../core/models/category";
import {QuizzesService} from "../../core/services/quizzes.service";
import {Question} from "../../core/models/question";
import {Quiz} from "../../core/models/quiz";
import {Imaged} from "../../core/models/imaged";
import {QuestionType} from "../../core/models/questionType";
import {QuestionOptions} from "../../core/models/questionOptions";
import {Tag} from "../../core/models/tag";
import {ActivatedRoute, Router} from "@angular/router";

import {COMMA, ENTER} from '@angular/cdk/keycodes';
import {FormControl} from '@angular/forms';
import {MatAutocomplete, MatAutocompleteSelectedEvent} from '@angular/material/autocomplete';
import {MatChipInputEvent} from '@angular/material/chips';
import {Observable} from 'rxjs';
import {map, startWith} from 'rxjs/operators';
import {SecurityService} from "../../core/services/security.service";

@Component({
  selector: 'app-create-quiz',
  templateUrl: './update-quiz.component.html',
  styleUrls: ['update-quiz.component.scss']
})
export class UpdateQuizComponent implements OnInit {
  categories: Category[];
  quiz: Quiz;

  visible = true;
  selectable = true;
  removable = true;
  separatorKeysCodes: number[] = [ENTER, COMMA];
  tagCtrl = new FormControl();
  filteredTags: Observable<string[]>;
  tags: string[] = [];

  questionTypes: string[] = ['SELECT_OPTION', 'SELECT_SEQUENCE', 'TRUE_FALSE', 'ENTER_ANSWER']

  messages: string[] = [];
  isInvalid: boolean = false;

  @ViewChild('tagInput') fruitInput: ElementRef<HTMLInputElement>;
  @ViewChild('auto') matAutocomplete: MatAutocomplete;

  constructor(
    private quizzesService: QuizzesService,
    private route: ActivatedRoute,
    private router: Router,
    private securityService: SecurityService) {

  }

  getCategories() {
    this.quizzesService.getCategories().subscribe(categories => {
        this.categories = categories;
        if (!this.quiz.id) {
          this.setCategory(categories[0].name)
        }
      },
      err => {
        console.log(err);
      });
  }

  getTags(): void {
    this.quizzesService.getTags()
      .subscribe(
        tags => {
          tags.forEach(function (value) {
            this.push(value.name);
          }, this.tags);
        },
        err => {
          console.log(err);
        })
  }

  createNewQuiz() {
    this.quiz = new Quiz();
    this.quiz.authorId = this.securityService.getCurrentId();
    this.setOptions(this.quiz.questions[0]);
  }


  ngOnInit(): void {
    const id = this.route.snapshot.paramMap.get('quizId');
    console.log(id);
    if (id) {
      this.quizzesService.getById(id, "").subscribe(
        data => {
          this.quiz = data;
        }, err => {
          console.log(err);
          this.createNewQuiz();
        });
    } else {
      this.createNewQuiz();
    }

    this.getCategories();
    this.getTags();


    this.filteredTags = this.tagCtrl.valueChanges.pipe(
      startWith(null),
      map((tags: string | null) => tags ? this._filter(tags) : this.tags.slice()));
  }

  processFile(imageInput: any, imaged: Imaged) {
    const file: File = imageInput.files[0];
    const reader = new FileReader();

    reader.addEventListener('load', (event: any) => {
      this.quizzesService.putImage(file).subscribe(
        id => {
          console.log("id=" + id);
          if (typeof id === "number") {
            imaged.image.src = event.target.result;
            imaged.image.src = imaged.image.src.substring(imaged.image.src.indexOf(',') + 1)
            imaged.imageId = id;
          }
        },
        error => {
          console.log(error);
        });
    });

    reader.readAsDataURL(file);
  }

  setQuestionOptions(question: Question, count: number) {
    question.options = [];
    for (let i = 0; i < count; i++) {
      question.options.push(new QuestionOptions(i + 1));
    }
  }

  setCorrectAnswer(question: Question, value: boolean) {
    question.options[0].isCorrect = value
  }

  setCategory(categoryStr: string) {
    this.quiz.category = this.categories.find((item) => item.name == categoryStr);
    this.quiz.categoryId = this.quiz.category.id;
  }

  setOptions(question: Question) {
    if (question.type == QuestionType.SELECT_OPTION || question.type == QuestionType.SELECT_SEQUENCE) {
      this.setQuestionOptions(question, 4);
    } else {
      this.setQuestionOptions(question, 1);
    }
    if (question.type == QuestionType.SELECT_OPTION) {
      question.options[0].isCorrect = true;
    }
  }

  onChangeTypeQuestion(question: Question, str: string) {
    question.type = QuestionType[str];
    console.log("changed");
    this.setOptions(question);
  }

  addQuestion() {
    let question = new Question();
    this.setOptions(question);
    this.quiz.questions = this.quiz.questions.concat(question);
  }

  isValid(): boolean {
    this.messages=[];
    if (this.quiz.name == null || this.quiz.name.length == 0) {
      //this.message = "Please enter name of quiz";
      this.messages[0]="quiz.errorValidation.noNameQuiz";
      return false
    }
    if (this.quiz.description == null || this.quiz.description.length == 0) {
      //this.message = "Please enter description of quiz";
      this.messages[0]="quiz.errorValidation.noDescQuiz";
      return false
    }
    if (this.quiz.questions.length < 1) {
      //this.message = "Please add one or more question";
      this.messages[0]="quiz.errorValidation.noQuestions";
      return false
    }
    for (let i = 0; i < this.quiz.questions.length; i++) {
      let question = this.quiz.questions[i];
      if ((question.content == null || question.content.length == 0) && question.imageId == -1) {
        //this.message = `Please enter name or add image of your ${i + 1} question`;
        this.messages[0]="quiz.errorValidation.noNameImage";
        this.messages[1]=`${i + 1}`
        this.messages[2]="quiz.errorValidation.question"

        return false
      }
      let countCorrectAnswer = 0;

      if (question.type != "TRUE_FALSE")
        for (let j = 0; j < question.options.length; j++) {
          if (question.options[j].isCorrect) {
            countCorrectAnswer++;
          }
          if (question.options[j].content.length == 0 && question.options[j].imageId == -1) {
            //this.message = `Please enter name ${(question.type == "ENTER_ANSWER" ? '' : ' or add image')}
            // for your ${j + 1} option ${i + 1} question`;
            this.messages[0]="quiz.errorValidation.noNameImage"
            this.messages[1]=`${j + 1}`
            this.messages[2]="quiz.errorValidation.option"
            this.messages[3]=`${i + 1}`
            this.messages[4]="quiz.errorValidation.question"
            return false
          }
        }
      if (question.type == "SELECT_OPTION") {
        if (countCorrectAnswer == 0) {
          //this.message = `Please chose one or more correct answer for your ${i + 1} question`;
          this.messages[0]="quiz.errorValidation.addMoreCorrectAnswer";
          this.messages[1]=`${i + 1}`
          this.messages[2]="quiz.errorValidation.question"
          return false
        }
        if (countCorrectAnswer == question.options.length) {
          //this.message = `Please chose less correct answer for your ${i + 1} question`;
          this.messages[0]="quiz.errorValidation.addLessCorrectAnswer";
          this.messages[1]=`${i + 1}`
          this.messages[2]="quiz.errorValidation.question"
          return false
        }
      }

    }
    return true;
  }

  removeQuestion(deletedQuestion: Question) {
    this.quiz.questions = this.quiz.questions.filter(question => question !== deletedQuestion);
  }

  send() {
    if (!this.isValid()) {
      this.isInvalid = true;
    } else {
      this.quizzesService.sendQuiz(this.quiz).subscribe(
        id => {
          console.log("id=" + id);
          this.isInvalid = false;
          this.router.navigate(['quiz/' + id])
        },
        error => {
          //this.message = `cant ${this.quiz.id ? 'update' : 'add'} quiz`;
          console.log(error);
        });
    }

  }


  add(event: MatChipInputEvent): void {
    const input = event.input;
    const value = event.value;

    // Add our tag
    if ((value || '').trim()) {
      if (!this.quiz.tags.find(value1 => value1.name == value)) {
        this.quiz.tags.push(new Tag(value.trim()));
      }
    }
    // Reset the input value
    if (input) {
      input.value = '';
    }

    this.tagCtrl.setValue(null);
  }

  remove(fruit: Tag): void {
    const index = this.quiz.tags.indexOf(fruit);

    if (index >= 0) {
      this.quiz.tags.splice(index, 1);
    }
  }

  selected(event: MatAutocompleteSelectedEvent): void {
    if (!this.quiz.tags.find(value => value.name == event.option.viewValue)) {
      this.quiz.tags.push(new Tag(event.option.viewValue));
    }
    this.fruitInput.nativeElement.value = '';
    this.tagCtrl.setValue(null);
  }

  private _filter(value: string): string[] {
    const filterValue = value.toLowerCase();

    return this.tags.filter(fruit => fruit.toLowerCase().indexOf(filterValue) === 0);
  }
}
