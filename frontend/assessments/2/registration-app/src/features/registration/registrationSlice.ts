import type { RegistrationResponse } from "../../types/registration";
import { createSlice, type PayloadAction } from "@reduxjs/toolkit";
import type { RegistrationState } from "../../types/registrationState";
import { submitRegistrationThunk } from "./registrationsThunk";

export const initialState: RegistrationState={
    form:{
        name:"",
        email:"",
        events:"",
        message:"",
        status:""
    },
    submitStatus:"queued",
    submitError:null,
    confirmation:null,
}
const registrationSlice=createSlice({
    name:"registration",
    initialState,

    reducers:{
        setName:(state,action:PayloadAction<string>)=>{
        state.form.name=action.payload;
        },
        setEmail:(state,action:PayloadAction<string>)=>{
        state.form.email=action.payload;
        },
        setEvent:(state,action:PayloadAction<string>)=>{
            state.form.events=action.payload;
        },
        setMessage:(state,action:PayloadAction<string>)=>{
        state.form.message=action.payload;
        },
    },
    extraReducers: (builder) => {
    builder
      .addCase(submitRegistrationThunk.pending,(state) => {
        state.submitStatus= "loading";
        state.submitError= null;
        state.confirmation= null;
      })
      .addCase(submitRegistrationThunk.fulfilled,(state, action: PayloadAction<RegistrationResponse>) => {
        state.submitStatus= "succeeded";
        state.confirmation= action.payload;
      })
      .addCase(submitRegistrationThunk.rejected,(state,action) => {
        state.submitStatus= "failed";
        state.submitError= action.error.message ?? "Failed to submit booking";
      });
  },
});

export const {
  setName,
  setEmail,
  setEvent,
  setMessage
}=registrationSlice.actions;
export default registrationSlice.reducer;