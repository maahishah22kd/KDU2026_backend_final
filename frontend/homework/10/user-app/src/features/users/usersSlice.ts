import { createApi, fetchBaseQuery } from '@reduxjs/toolkit/query/react';
import type { UsersListResponse } from '../../types/user';
const BASE_URL = import.meta.env.VITE_API_BASE_URL ?? "https://dummyjson.com";
import type { User } from '../../types/user';
type AddUserRequest = Pick<User, "firstName" | "lastName" | "email" | "age">;

export const userApi = createApi({
  reducerPath: 'usersApi',

  baseQuery: fetchBaseQuery({ baseUrl: BASE_URL }),
  tagTypes: ["Users"],
    keepUnusedDataFor: 300,          
  refetchOnMountOrArgChange: false, 
  refetchOnFocus: false,            
  refetchOnReconnect: false,        
  endpoints: (builder) => ({
    getUsers: builder.query<UsersListResponse,void>({
      query: () => '/users',
      providesTags: ["Users"],
    }),
    getUserById: builder.query<User, string>({
    query: (id) => `/users/${id}`,
    }),

    addUser: builder.mutation<User,AddUserRequest>({
        query:(body)=>({
            url:"/users/add",
            method:"POST",
            body,
        }),
        invalidatesTags: ["Users"],
    })
  }),
});


export const { useGetUsersQuery,useAddUserMutation,useGetUserByIdQuery} = userApi;