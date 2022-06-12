import {Injectable} from '@angular/core';

import {HttpClient, HttpHeaders} from '@angular/common/http';

import {User} from '../models/user';
import {url} from '../../../../environments/environment.prod';
import {map} from "rxjs/operators";
import {JwtHelperService} from '@auth0/angular-jwt';
import {Observable} from "rxjs";
import {Lang} from "../models/lang";

@Injectable({
  providedIn: 'root'
})
export class AuthenticationService {

  httpOptions = {
    headers: new HttpHeaders({'Content-Type': 'application/json'})
  };
  providers: [
  ]

  constructor(private http: HttpClient, private jwtHelper: JwtHelperService) {
  }

  login(user: User): Observable<any> {
    let username = user.login;
    let password = user.password;
    return this.http.post<any>(`${url}/login`, {username, password}).pipe(
      map(
        userData => {
          let tokenStr = 'Bearer ' + userData.token;
          localStorage.setItem('token', tokenStr);
          return userData;
        }
      )
    );
  }

  setLang(id: number, lang:Lang){
    return this.http.put(`${url}/users/${id}/setLang`, lang, this.httpOptions)
  }

  register(user: User) {
    return this.http.post<User>(`${url}/registration`, user, this.httpOptions);
  }

  createAdmin(user: User){
    console.log(user);
    return this.http.post<User>(`${url}/users/create`, user, this.httpOptions);
  }

  confirmMail(token: string) {
    return this.http.get<boolean>(`${url}/registration/${token}`);
  }

  resetPass(email: string) {
    return this.http.post<string>(`${url}/pass-recovery`, email, this.httpOptions);
  }

  confirmResetPass(token: string) {
    return this.http.get<boolean>(`${url}/pass-recovery/${token}`);
  }

  createNewPass(token: string, password: string) {
    return this.http.put<string>(`${url}/pass-recovery/${token}`, password, this.httpOptions);
  }

  logOut() {
    localStorage.removeItem('token');
  }

  public isAuthenticated(): boolean {
    const token = localStorage.getItem('token');
    // Check whether the token is expired and return
    // true or false
    if (!token) {
      return false;
    }
    if(!this.jwtHelper){
      return false;
    }
    if (this.jwtHelper.isTokenExpired(token)) {
      localStorage.removeItem('token');
      return false;
    }
    return true;

  }
}
