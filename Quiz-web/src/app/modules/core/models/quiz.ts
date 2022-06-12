import {Tag} from "./tag";
import {Category} from "./category";
import {Question} from "./question";
import {Imaged} from "./imaged";
import {Image} from "./image";
import {QuizStatus} from "./quizStatus";

export class Quiz implements Imaged {
  id: number;
  name: string;
  authorId: number;
  description: string = "";
  category: Category = new Category();
  categoryId: number;
  publishedDate: string;
  questionNumber: number;
  maxScore: number;
  tags: Tag[] = [];
  questions: Question[] = [new Question];
  imageId: number = -1;
  image: Image = new Image();
  status: QuizStatus = QuizStatus.UNPUBLISHED;
  isFavorite: boolean = false;
}
