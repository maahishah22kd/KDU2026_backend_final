const BASE_URL = import.meta.env.VITE_API_BASE_URL;
import type { Registration,RegistrationResponse } from "../types/registration";
import type { ConfirmationResponse } from "../types/registrationConfirmation";

async function request<T>(path:string, options?: RequestInit): Promise<T>{
    const res = await fetch(`${BASE_URL}${path}`, options);
  if(!res.ok){
    const text = await res.text().catch(() => "");
    throw new Error(`API error ${res.status}:${text||res.statusText}`);
  }
  return res.json() as Promise<T>;
}

export function createrRegistration(registration: Registration): Promise<RegistrationResponse>{
    return request<RegistrationResponse>("/register",{
        method:"POST",
        body: JSON.stringify(registration),
    });
}

export function getStatus(): Promise<ConfirmationResponse> {
  return request<ConfirmationResponse>("/config");
}