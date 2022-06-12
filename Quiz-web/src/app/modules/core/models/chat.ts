import {User} from "./user";

export class Chat {
  id: number;
  name: string;
  creationDate: Date;
  users: User[] = [];
}
