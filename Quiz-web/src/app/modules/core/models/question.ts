import {QuestionType} from './questionType';
import {Imaged} from "./imaged";
import {QuestionOptions} from "./questionOptions";
import {Quiz} from "./quiz";
import {Image} from "./image";

export class Question implements Imaged {
  id: number;
  type: QuestionType = QuestionType.SELECT_OPTION;
  content: string;
  score: number;
  imageId: number = -1;
  options: QuestionOptions[] = [];
  image: Image = new Image();

}
