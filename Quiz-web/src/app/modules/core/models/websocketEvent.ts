import {Question} from "./question";
import {Users} from "./gameResults";
import {NotificationDto} from "./notificationDto";
import {Message} from "./message";

export class WebsocketEvent {
  players: String[];
  question: Question;
  gameResults: Users;
  message: Message;
  type:EventType;
  notification: NotificationDto;
  currQuestion: number;
}

export enum EventType {
  RESULTS = "RESULTS",
  QUESTION = "QUESTION",
  PLAYERS = "PLAYERS",
  MESSAGE = "MESSAGE",
  NOTIFICATION = "NOTIFICATION"
}
