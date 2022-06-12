import {Injectable} from '@angular/core';

import {HttpClient, HttpHeaders} from '@angular/common/http';
import {Observable, of} from 'rxjs';
import {catchError} from "rxjs/operators";

import {url} from "../../../../environments/environment.prod";
import {GameDto} from "../models/gameDto";
import {UserDto} from "../models/userDto";
import {Question} from "../models/question";
import {User} from "../models/user";

@Injectable({
  providedIn: 'root'
})
export class PlayGameService {

  httpOptions = {
    headers: new HttpHeaders({'Content-Type': 'application/json'})
  };

  constructor(private http: HttpClient) {
  }

  /* getQuizzes(params: string, currentCount: number): Observable<Quiz[]> {
     return this.http.get<Quiz[]>(`${url}/quizzes${params}&pageNumber=${currentCount / countOnPage}&countOnPage=${countOnPage}`)
       .pipe(
         catchError(this.handleError<Quiz[]>([]))
       );
   }

   getCategories(): Observable<Category[]> {
     return this.http.get<Category[]>(`${url}/categories`)
       .pipe(
         catchError(this.handleError<Quiz[]>([]))
       );
   }

   getTags(): Observable<Tag[]> {
     return this.http.get<Tag[]>(`${url}/tags`)
       .pipe(
         catchError(this.handleError<Tag[]>([]))
       );
   }

   putImage(image: File) {
     const uploadData = new FormData();
     uploadData.append('myFile', image, "name");
     return this.http.put(`${url}/image`, uploadData);
   }*/

  sendGame(game: GameDto) {
    return this.http.post<string>(`${url}/game/`, game, this.httpOptions).pipe(
      catchError(this.handleError<String>(null))
    );
  }

  sendJoinedUser(userId: number, gameId: string) {
    return this.http.post<UserDto>(`${url}/game/${gameId}/joinedUser`, userId, this.httpOptions);
  }

  /*getById(id: string) {
    return this.http.get<Quiz>(`${url}/quiz/${id}`).pipe(
      catchError(this.handleError<Quiz>())
    );
  }*/

  private handleError<T>(result?: T) {
    return (error: any): Observable<T> => {

      console.error(error); // log to console instead

      return of(result as T);
    };
  }

  getJoinedPlayers(gameId: string): Observable<String[]> {
    return this.http.get<String[]>(`${url}/game/${gameId}/joinedUser`)
      .pipe(
        catchError(this.handleError<String[]>([]))
      );
  }

  getGame(gameId: string) {
    return this.http.get<GameDto>(`${url}/game/${gameId}`)
      .pipe(
        catchError(this.handleError<GameDto>(null))
      );
  }

  getCurrentQuestion(gameId: string, id: number) {
    return this.http.get<Question>(`${url}/game/${gameId}/user/${id}`)
      .pipe(
        catchError(this.handleError<Question>(null))
      );
  }

  inviteToGame(selectedFriend: number, gameId: string) {
    return this.http.post(`${url}/game/${gameId}/inviteFriend/${selectedFriend}`, this.httpOptions);
  }
}
