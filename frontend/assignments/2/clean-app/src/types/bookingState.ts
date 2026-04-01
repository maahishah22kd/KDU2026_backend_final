import type { Booking } from "./booking";

export interface BookingState {
  form: Booking;
  submitStatus: "idle"|"loading"|"succeeded"|"failed";
  submitError: string|null;

  confirmation: {
    bookingId: string;
    message: string;
  }|null;
}
