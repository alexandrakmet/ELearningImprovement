import {Injectable} from '@angular/core';
import decode from 'jwt-decode';
import {Role} from "../models/role";

@Injectable({providedIn: 'root'})
export class SecurityService {

  private role: Role;
  private id: number;

  constructor() {
  }

  public getCurrentId() {
    const token = localStorage.getItem('token');
    if (token) {
      const tokenPayload = decode(token);
      return this.id = tokenPayload.userId;
    } else {
      return null;
    }
  }

  public getCurrentRole() {
    const token = localStorage.getItem('token');
    if (token) {
      const tokenPayload = decode(token);
      return this.role = tokenPayload.role;
    } else {
      return null;
    }

  }
}
