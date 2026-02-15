const BASE_URL = import.meta.env.VITE_API_BASE_URL;
import type { Booking, BookingResponse } from "../types/booking";
import type { ConfigResponse } from "../types/config";

async function request<T>(path:string, options?: RequestInit): Promise<T>{
    const res = await fetch(`${BASE_URL}${path}`, options);
  if(!res.ok){
    const text = await res.text().catch(() => "");
    throw new Error(`API error ${res.status}:${text||res.statusText}`);
  }
  return res.json() as Promise<T>;
}

export function getConfig(): Promise<ConfigResponse> {
  return request<ConfigResponse>("/config");
}

export function createBooking(booking: Booking): Promise<BookingResponse>{
    return request<BookingResponse>("/booking",{
        method:"POST",
        body: JSON.stringify(booking),
    });
}