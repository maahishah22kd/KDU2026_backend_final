export interface User{
    id:number;
    firstName:string;
    lastName: string;

    email: string;
    phone: string;
    age: number;

    image: string;
}

export interface UsersListResponse{
  users: User[];
  total: number;
}