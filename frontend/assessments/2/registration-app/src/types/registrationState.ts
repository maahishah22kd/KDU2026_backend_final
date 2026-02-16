import type { Registration } from "./registration";

export interface RegistrationState{
    form: Registration,
    submitStatus: "queued"|"loading"|"succeeded"|"failed";
     submitError: string|null;

    confirmation: {
    registrationId: string;
    message: string;
  }|null;
}