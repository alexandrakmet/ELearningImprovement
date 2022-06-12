import {Injectable} from '@angular/core';

import {HttpClient, HttpHeaders} from '@angular/common/http';
import {Observable, of} from 'rxjs';
import {catchError} from "rxjs/operators";

import {Quiz} from '../models/quiz'
import {Category} from '../models/category'
import {countOnPage, url} from "../../../../environments/environment.prod";
import {Tag} from "../models/tag";

@Injectable({
  providedIn: 'root'
})
export class QuizzesService {

  httpOptions = {
    headers: new HttpHeaders({'Content-Type': 'application/json'})
  };

  constructor(private http: HttpClient) {
  }

  getQuizzes(params: string, currentCount: number): Observable<Quiz[]> {
    return this.http.get<Quiz[]>(`${url}/quizzes${params}&pageNumber=${currentCount / countOnPage}&countOnPage=${countOnPage}`)
      .pipe(
        catchError(this.handleError<Quiz[]>([]))
      );
  }

  getUserQuizzes(type: number, currentCount: number, userId: number): Observable<Quiz[]> {
    let str: String = "";
    switch (type) {
      case 0:
        str = "userFavoriteQuizzes";
        break;
      case 1:
        str = "userCompletedQuizzes";
        break;
      case 2:
        str = "userCreatedQuizzes";
        break;
      default:
        return null;

    }
    return this.http.get<Quiz[]>(`${url}/${str}?pageNumber=${currentCount / countOnPage}&countOnPage=${countOnPage}&userId=${userId}`)
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
  }

  sendQuiz(quiz: Quiz) {
    if (quiz.id) {
      return this.http.put<string>(`${url}/quiz/`, quiz, this.httpOptions);
    } else {
      return this.http.post<string>(`${url}/quiz/`, quiz, this.httpOptions);
    }
  }

  updateQuizStatus(quiz: Quiz) {
    return this.http.put(`${url}/quiz/${quiz.id}/setStatus`, quiz.status, this.httpOptions);
  }

  updateQuizIsFavorite(quiz: Quiz, userId: number) {
    return this.http.put(`${url}/quiz/${quiz.id}/user/${userId}/setFavorite`, quiz.isFavorite, this.httpOptions);
  }

  getById(id: string, userIdStr: string) {
    return this.http.get<Quiz>(`${url}/quiz/${id + userIdStr}`).pipe(
      catchError(this.handleError<Quiz>())
    );
  }

  private handleError<T>(result?: T) {
    return (error: any): Observable<T> => {

      console.error(error); // log to console instead

      return of(result as T);
    };
  }

}
