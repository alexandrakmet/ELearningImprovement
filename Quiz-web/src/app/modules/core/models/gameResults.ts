export class Users {
  users: UserDto[];
  isFinal: boolean;
}

export class UserDto {
  login: string;
  id:number;
  score: number;
}
