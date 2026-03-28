import { createAsyncThunk } from "@reduxjs/toolkit";
import type { ConfirmationResponse } from "../../types/registrationConfirmation";
const BASE_URL = import.meta.env.VITE_API_BASE_URL;

export const fetchStatusThunk=createAsyncThunk<ConfirmationResponse>(
  "status/fetch",
  async()=>{
    const response = await fetch(`${BASE_URL}/status`);
    if(!response.ok){
      throw new Error(`Failed to fetch products: ${response.status}`);
    }
    const data:ConfirmationResponse= await response.json();
    return data;
  }
);