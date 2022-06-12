import {Injectable} from '@angular/core';

import {HttpClient, HttpHeaders} from '@angular/common/http';
import {Observable, of} from 'rxjs';
import {catchError} from "rxjs/operators";

import {url} from "../../../../environments/environment.prod";
import {Announcement} from "../models/announcement";

@Injectable({
  providedIn: 'root'
})

export class AnnouncementService {

  httpOptions = {
    headers: new HttpHeaders({'Content-Type': 'application/json'})
  };

  constructor(private http: HttpClient) {
  }

  getAnnouncements(currentCount: number): Observable<Announcement[]> {
    return this.http.get<Announcement[]>(`${url}/announcements?count=${currentCount}`)
      .pipe(
        catchError(this.handleError<Announcement[]>([]))
      );
  }

  getAnnouncementsByRole(currentCount: number, isPublished:boolean): Observable<Announcement[]> {
    return this.http.get<Announcement[]>(`${url}/announcements?pageNumber=${currentCount/10}&isPublished=${isPublished}`)
      .pipe(
        catchError(this.handleError<Announcement[]>([]))
      );
  }

  getAnnouncementsByID(currentCount: number, isPublished:boolean): Observable<Announcement[]> {
    return this.http.get<Announcement[]>(`${url}/announcements?pageNumber=${currentCount/10}&isPublished=${isPublished}`)
      .pipe(
        catchError(this.handleError<Announcement[]>([]))
      );
  }

  putImage(image: File) {
    const uploadData = new FormData();
    uploadData.append('myFile', image, "name");
    return this.http.put(`${url}/image`, uploadData);
  }

  updateAnnouncement(announcement: Announcement) {
    return this.http.put<string>(`${url}/announcement/`, announcement, this.httpOptions);
  }

  sendAnnouncement(announcement: Announcement) {
    return this.http.post<string>(`${url}/announcement/`, announcement, this.httpOptions);
  }

  getById(id: string) {
    return this.http.get<Announcement>(`${url}/announcement/${id}`).pipe(
      catchError(this.handleError<Announcement>())
    );
  }

  private handleError<T>(result?: T) {
    return (error: any): Observable<T> => {

      console.error(error);

      return of(result as T);
    };
  }

  deleteAnnouncement(announcement: Announcement) {
    return this.http.delete(`${url}/announcement/${announcement.id}`);
  }
}
