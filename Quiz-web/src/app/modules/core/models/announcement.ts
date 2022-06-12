import {Imaged} from "./imaged";
import {Image} from "./image";

export class Announcement implements Imaged {
  id: number;
  title: string;
  subtitle: string;
  fullText: string;
  authorLogin: string;
  authorId: number;
  isPublished: boolean;
  createdDate: Date;
  imageId: number = -1;
  image: Image;
}
