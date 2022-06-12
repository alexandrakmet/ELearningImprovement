import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {url} from "../../../../environments/environment.prod";
import {catchError} from "rxjs/operators";
import {NotificationDto} from "../models/notificationDto";
import {Observable, of} from "rxjs";
import {NotificationFilters} from "../models/notificationFilters";

@Injectable({
  providedIn: 'root'
})
export class NotificationService {
  httpOptions = {
    headers: new HttpHeaders({'Content-Type': 'application/json'})
  };


  constructor(private http: HttpClient) {
  }

  getNotificationsByUserId(userId: number): Observable<NotificationDto[]> {
    return this.http.get<NotificationDto[]>(`${url}/notifications/${userId}`)
      .pipe(
        catchError(this.handleError<NotificationDto[]>([]))
      );
  }

  getMessagesByUserId(userId: number): Observable<NotificationDto[]> {
    return this.http.get<NotificationDto[]>(`${url}/notifications/messages/${userId}`)
      .pipe(
        catchError(this.handleError<NotificationDto[]>([]))
      );
  }

  updateNotificationView(notification: NotificationDto) {
    return this.http.put<string>(`${url}/notification`, notification, this.httpOptions);
  }

  deleteAllByUserId(userId: number): Observable<{}> {
    return this.http.delete(`${url}/notification/user/${userId}`, this.httpOptions)
      .pipe(
        catchError(this.handleError('deleteAllByUserId'))
      );
  }

  deleteNotificationById(id: number): Observable<{}> {
    return this.http.delete(`${url}/notification/${id}`, this.httpOptions)
      .pipe(
        catchError(this.handleError('deleteAllByUserId'))
      );
  }


  getSettingsByUserId(userId: number): Observable<NotificationFilters> {
    return this.http.get<NotificationFilters>(`${url}/notification/settings/${userId}`)
      .pipe(
        catchError(this.handleError<NotificationFilters>())
      );
  }

  updateSettings(settings: NotificationFilters) {
    return this.http.put<string>(`${url}/notification/settings`, settings, this.httpOptions);
  }

  private handleError<T>(result?: T) {
    return (error: any): Observable<T> => {

      console.error(error); // log to console instead

      return of(result as T);
    };
  }
}
