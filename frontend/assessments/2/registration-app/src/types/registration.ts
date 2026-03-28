export interface Registration{
    name:string,
    email:string,
    events:string,
    message:string,
    status:string
}

export interface RegistrationResponse{
    registrationId:string,
    message:string
}