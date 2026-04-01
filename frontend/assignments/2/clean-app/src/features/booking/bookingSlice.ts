import { createSlice, type PayloadAction } from "@reduxjs/toolkit";
import type { BookingState } from "../../types/bookingState";
import type { Booking,BookingResponse } from "../../types/booking";
import { submitBookingThunk } from "./bookingsThunks";
export const initialState: BookingState={
  form:{
    cleaningType:null,
    frequency:null,
    bedrooms:1,
    bathrooms:1,
    extras:[],
    requirements:"",
    hours:1,
    date:"",
    timeSlot:null,
    cardNumber:"",
    expiration:"",
    cvv:"",
    name:"",
    email:"",
    phone:"",
    address:"",
    pincode:"",
    termsAccepted:false,
  },

  submitStatus:"idle",
  submitError:null,
  confirmation:null,
};



const bookingsSlice = createSlice({
  name: "bookings",
  initialState,

  reducers: {
    setCleaningType:(state,action:PayloadAction<string>)=>{
        state.form.cleaningType=action.payload;
    },
    setFrequency:(state,action:PayloadAction<string>)=>{
      state.form.frequency=action.payload;
    },
    setBedrooms:(state,action:PayloadAction<number>)=>{
      state.form.bedrooms= action.payload;
    },
    setBathrooms:(state,action:PayloadAction<number>)=>{
      state.form.bathrooms=action.payload;
    },
    setHours:(state,action:PayloadAction<number>)=>{
      state.form.hours= action.payload;
    },

    toggleExtra:(state,action: PayloadAction<string>)=>{
      const id= action.payload;
      const exists=state.form.extras.includes(id);
      state.form.extras= exists
        ? state.form.extras.filter((x)=>x!==id)
        : [...state.form.extras,id];
    },
    setRequirements:(state,action: PayloadAction<string>)=>{
      state.form.requirements=action.payload;
    },
    setDate:(state,action: PayloadAction<string>)=>{
      state.form.date=action.payload;
    },
    setTimeSlot:(state,action: PayloadAction<string>)=>{
      state.form.timeSlot=action.payload;
    },

    updateCardField:(
      state,
      action: PayloadAction<{
        field: "cardNumber"|"expiration"|"cvv"|"name";
        value: string;
      }>
    )=>{
      const{field,value}= action.payload;
      (state.form as Booking)[field]= value;
    },

    updatePersonalField:(
      state,
      action: PayloadAction<{
        field: "email"|"phone"|"address"|"pincode";
        value: string;
      }>
    )=>{
      const{field,value}= action.payload;
      (state.form as Booking)[field]= value;
    },

     setTermsAccepted: (state, action: PayloadAction<boolean>) => {
      state.form.termsAccepted=action.payload;
    },

     resetBooking: (state) => {
      state.form = initialState.form;
      state.submitStatus = "idle";
      state.submitError = null;
      state.confirmation = null;
    },
  },

  extraReducers: (builder) => {
    builder
      .addCase(submitBookingThunk.pending,(state) => {
        state.submitStatus= "loading";
        state.submitError= null;
        state.confirmation= null;
      })
      .addCase(submitBookingThunk.fulfilled,(state, action: PayloadAction<BookingResponse>) => {
        state.submitStatus= "succeeded";
        state.confirmation= action.payload;
      })
      .addCase(submitBookingThunk.rejected,(state,action) => {
        state.submitStatus= "failed";
        state.submitError= action.error.message ?? "Failed to submit booking";
      });
  },
});

export const {
  setCleaningType,
  setFrequency,
  setBedrooms,
  setBathrooms,
  setHours,
  toggleExtra,
  setRequirements,
  setDate,
  setTimeSlot,
  updateCardField,
  updatePersonalField,
  setTermsAccepted,
  resetBooking,
}=bookingsSlice.actions;

export default bookingsSlice.reducer;
