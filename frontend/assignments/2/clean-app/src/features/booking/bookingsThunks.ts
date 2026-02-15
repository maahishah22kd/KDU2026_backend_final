import { createAsyncThunk } from "@reduxjs/toolkit";
import type { Booking, BookingResponse } from "../../types/booking";
import type { ConfigResponse } from "../../types/config";
const BASE_URL = import.meta.env.VITE_API_BASE_URL;

export const fetchConfigThunk=createAsyncThunk<ConfigResponse>(
  "config/fetch",
  async()=>{
    const response = await fetch(`${BASE_URL}/config`);
    if(!response.ok){
      throw new Error(`Failed to fetch products: ${response.status}`);
    }
    const data:ConfigResponse= await response.json();
    return data;
  }
);

export const submitBookingThunk= createAsyncThunk<BookingResponse, Booking>(
  "booking/submit",
  async(booking:Booking)=>{
    const response = await fetch(`${BASE_URL}/booking`, {
      method:"POST",
      headers:{
        "Content-Type": "application/json",
      },
      body: JSON.stringify(booking),
    });

    if (!response.ok) {
      throw new Error(`Failed to submit booking: ${response.status}`);
    }

    const data:BookingResponse=await response.json();
    return data;
  }
);


