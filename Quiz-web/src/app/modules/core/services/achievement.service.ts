import {Injectable} from '@angular/core';

import {HttpClient, HttpHeaders} from '@angular/common/http';
import {Observable, of} from 'rxjs';

import {url} from "../../../../environments/environment.prod";
import {Achievement} from "../models/achievement";
import {catchError} from "rxjs/operators";
import {AchievementCharacteristic} from "../models/achievementCondition";
import {SecurityService} from "./security.service";

@Injectable({
  providedIn: 'root'
})
export class AchievementService {

  httpOptions = {
    headers: new HttpHeaders({'Content-Type': 'application/json'})
  };

  constructor(
    private http: HttpClient) {
  }

  sendAchievement(achievement: Achievement){
      return this.http.post<string>(`${url}/achievement/create`, achievement, this.httpOptions);
  }

  getCharacteristics(): Observable<AchievementCharacteristic[]> {
    return this.http.get<AchievementCharacteristic[]>(`${url}/achievement/characteristics`)
      .pipe(
        catchError(this.handleError<AchievementCharacteristic[]>([]))
      );
  }

  getUserAchievements(currentId: number): Observable<Achievement[]> {
    return this.http.get<Achievement[]>(`${url}/profile/${currentId}/achievements/`)
      .pipe(
        catchError(this.handleError<Achievement[]>([]))
      );
  }

  private handleError<T>(result?: T) {
    return (error: any): Observable<T> => {
      console.error(error);
      return of(result as T);
    };
  }

  recalculateAchievements(){
      return this.http.put<string>(`${url}/achievement/recalculate`, "" , this.httpOptions);
  }
}
