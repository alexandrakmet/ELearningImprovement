import {Injectable} from '@angular/core';

import {HttpClient} from '@angular/common/http';
import {Observable, of} from 'rxjs';
import {catchError} from "rxjs/operators";

import {countOnPage, url} from "../../../../environments/environment.prod";
import {Activity} from "../models/activity";

@Injectable({
  providedIn: 'root'
})

export class ActivitiesService {

  constructor(private http: HttpClient) {
  }

  getFilterActivitiesPage(userId: number, categoryFilter: boolean[], currentCount: number): Observable<Activity[]> {
    return this.http.get<Activity[]>(`${url}/activities/filter/${userId}?categoryFilter=${categoryFilter}&pageNumber=${currentCount/countOnPage}`)
      .pipe(
        catchError(this.handleError<Activity[]>([]))
      );
  }

  getActivitiesPageByUserId(userId: number, currentCount: number): Observable<Activity[]> {
    return this.http.get<Activity[]>(`${url}/activities/${userId}?pageNumber=${currentCount/countOnPage}`)
      .pipe(
        catchError(this.handleError<Activity[]>([]))
      );
  }

  private handleError<T>(result?: T) {
    return (error: any): Observable<T> => {

      console.error(error);

      return of(result as T);
    };
  }
}
