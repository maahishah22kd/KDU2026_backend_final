const BASE_URL = import.meta.env.VITE_API_BASE_URL ?? "https://dummyjson.com";
import type { User, UsersListResponse } from "../types/user";

async function request<T>(path:string): Promise<T>{
    const res = await fetch(`${BASE_URL}${path}`);

  if (!res.ok) {
    const text = await res.text().catch(() => "");
    throw new Error(`API error ${res.status}: ${text || res.statusText}`);
  }

  return res.json() as Promise<T>;
}

export function getAllUsers(): Promise<UsersListResponse> {
  return request<UsersListResponse>("/users");
}

export function getUserById(id: string|undefined): Promise<User> {
  return request<User>(`/users/${id}`);
}

