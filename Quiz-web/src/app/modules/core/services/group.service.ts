import { Injectable } from '@angular/core';

import {HttpClient, HttpHeaders} from '@angular/common/http';
import {Observable, of} from 'rxjs';
import {catchError} from "rxjs/operators";

import {url} from "../../../../environments/environment.prod";
import {Group} from "../models/group";
import {User} from "../models/user";


@Injectable({
  providedIn: 'root'
})
export class GroupService {
  httpOptions = {
    headers: new HttpHeaders({'Content-Type': 'application/json'})
  };

  constructor(private http: HttpClient) { }



  createGroup(chat: Group, id: number) {
    return this.http.post<Group>(`${url}/users/${id}/createGroup`, chat, this.httpOptions);
  }

  inviteToGroup(userId: number, chatId: string) {
    return this.http.post<Group>(`${url}/users/${userId}/group/${chatId}/invite`, this.httpOptions);
  }

  getUserGroups(id: number) {
    return this.http.get<Group[]>(`${url}/users/${id}/groups`)
      .pipe(
        catchError(this.handleError<Group[]>([]))
      );
  }

  getGroupMembers(id: string) {
    return this.http.get<User[]>(`${url}/group/${id}/members`)
      .pipe(
        catchError(this.handleError<User[]>([]))
      );
  }

  getGroup(chatId: string) {
    return this.http.get<Group>(`${url}/group/${chatId}`)
      .pipe(
        catchError(this.handleError<Group>(null))
      );
  }


  private handleError<T>(result?: T) {
    return (error: any): Observable<T> => {
      console.error(error);

      return of(result as T);
    };
  }

}
