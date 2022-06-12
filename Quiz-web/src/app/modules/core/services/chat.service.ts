import { Injectable } from '@angular/core';

import {HttpClient, HttpHeaders} from '@angular/common/http';
import {Observable, of} from 'rxjs';
import {catchError} from "rxjs/operators";

import {url} from "../../../../environments/environment.prod";
import {Chat} from "../models/chat";
import {Message} from "../models/message";
import {User} from "../models/user";

@Injectable({
  providedIn: 'root'
})
export class ChatService {
  httpOptions = {
    headers: new HttpHeaders({'Content-Type': 'application/json'})
  };

  constructor(private http: HttpClient) { }

  updateChat(chat: Chat) {
    return this.http.put<Chat>(`${url}/chat`, chat, this.httpOptions).pipe(
      catchError(this.handleError<Chat>(null))
    );
  }

  createChat(chat: Chat, id: number) {
    return this.http.post<Chat>(`${url}/users/${id}/createChat`, chat, this.httpOptions);
  }

  inviteToChat(user: User, chatId: number) {
    return this.http.post<Chat>(`${url}/users/${user.id}/chat/${chatId}/invite`, this.httpOptions);
  }

  leaveChat(id:number, chatId:number){
    return this.http.delete<string>(`${url}/users/${id}/chat/${chatId}`);
  }

  getUserChats(id: number) {
    return this.http.get<Chat[]>(`${url}/users/${id}/chats`)
      .pipe(
        catchError(this.handleError<Chat[]>([]))
      );
  }

  getChat(chatId: number) {
    return this.http.get<Chat>(`${url}/chat/${chatId}`)
      .pipe(
        catchError(this.handleError<Chat>(null))
      );
  }

  getMessages(chatId: number, currentCount: number): Observable<Message[]> {
    return this.http.get<Message[]>(`${url}/chat/${chatId}/messages?pageNumber=${Math.floor(currentCount/10)}`)
      .pipe(
        catchError(this.handleError<Message[]>([]))
      );
  }

  checkChatAffiliation(id: number, chatId: number) {
    return this.http.get<boolean>(`${url}/users/${id}/chat/${chatId}/check`)
      .pipe(
        catchError(this.handleError<boolean>(false))
      );
  }

  private handleError<T>(result?: T) {
    return (error: any): Observable<T> => {
      console.error(error);

      return of(result as T);
    };
  }

}
