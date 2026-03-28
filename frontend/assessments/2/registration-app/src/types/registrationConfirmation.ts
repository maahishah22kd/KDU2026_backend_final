export interface ConfirmationResponse{
    registrationId:string,
    form:{
        name:string,
        email:string,
        events:string,
        message:string
    }
}