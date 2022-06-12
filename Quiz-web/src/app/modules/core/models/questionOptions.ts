import {Imaged} from "./imaged";
import {Image} from "./image";


export class QuestionOptions implements Imaged {
  id: number = -1;

  content: string = "";
  isCorrect: boolean = false;
  sequenceOrder: number = -1;

  imageId: number = -1;
  image: Image = new Image();

  constructor(sequenceOrder: number) {
    this.sequenceOrder = sequenceOrder;
  };

}
