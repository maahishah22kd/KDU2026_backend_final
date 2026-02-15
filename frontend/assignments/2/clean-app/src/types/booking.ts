export interface Booking{
    cleaningType:string|null,
    frequency:string|null,
    bedrooms:number,
    bathrooms:number,
    extras:string[],
    requirements?:string,
    hours:number,
    date:string,
    timeSlot: string|null,

    cardNumber:string,
    expiration:string,
    cvv:string,
    name:string,
    email:string,
    phone:string,
    address:string,
    pincode:string,
    termsAccepted:boolean;
};


export interface BookingResponse {
  bookingId: string;
  message: string;
};