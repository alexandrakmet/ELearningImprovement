import {User} from "./user";

export class Message {
  id: number;
  chatId: number;
  authorId: number;
  author: User;
  creationDate: Date;
  messageText: string;
}
