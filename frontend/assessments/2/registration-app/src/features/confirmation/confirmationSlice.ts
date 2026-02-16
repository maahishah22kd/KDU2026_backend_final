import { createSlice } from "@reduxjs/toolkit";
import type { ConfirmationState } from "../../types/registrationConfirmationState";
import { fetchStatusThunk } from "./confirmationThunk";

export const initialState: ConfirmationState = {
  data:null,
  status:"queued",
  error:null,
};

const confirmationSlice = createSlice({
  name: "config",
  initialState,

  reducers: {},
  extraReducers:(builder)=>{
    builder
      .addCase(fetchStatusThunk.pending,(state)=>{
        state.status= "loading";
        state.error= null;
      })
      .addCase(fetchStatusThunk.fulfilled,(state, action)=>{
        state.status="succeeded";
        state.data=action.payload;
      })
      .addCase(fetchStatusThunk.rejected,(state, action)=>{
        state.status= "failed";
        state.error= action.error.message??"Failed to load config";
      });
  },
});

export default confirmationSlice.reducer;