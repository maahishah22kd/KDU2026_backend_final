import { createAsyncThunk } from "@reduxjs/toolkit";
import type { Registration, RegistrationResponse } from "../../types/registration";
const BASE_URL = import.meta.env.VITE_API_BASE_URL;

export const submitRegistrationThunk= createAsyncThunk<RegistrationResponse, Registration>(
  "register/submit",
  async(register:Registration)=>{
    const response = await fetch(`${BASE_URL}/register`, {
      method:"POST",
      headers:{
        "Content-Type": "application/json",
      },
      body: JSON.stringify(register),
    });
    if (!response.ok) {
      throw new Error(`Failed to submit registration: ${response.status}`);
    }

    const data:RegistrationResponse=await response.json();
    return data;
  }
);