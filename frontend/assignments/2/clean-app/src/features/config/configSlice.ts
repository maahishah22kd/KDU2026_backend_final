import { createSlice } from "@reduxjs/toolkit";
import type { ConfigState } from "../../types/configState";
import { fetchConfigThunk } from "../booking/bookingsThunks";

export const initialState: ConfigState = {
  data:null,
  status:"idle",
  error:null,
};

const configSlice = createSlice({
  name: "config",
  initialState,

  reducers: {},
  extraReducers:(builder)=>{
    builder
      .addCase(fetchConfigThunk.pending,(state)=>{
        state.status= "loading";
        state.error= null;
      })
      .addCase(fetchConfigThunk.fulfilled,(state, action)=>{
        state.status="succeeded";
        state.data=action.payload;
      })
      .addCase(fetchConfigThunk.rejected,(state, action)=>{
        state.status= "failed";
        state.error= action.error.message??"Failed to load config";
      });
  },
});

export default configSlice.reducer;
